let chart;

document.addEventListener("DOMContentLoaded", () => {
  loadDashboard();
  document.getElementById("refresh-button").addEventListener("click", refreshDashboard);
  document.getElementById("import-form").addEventListener("submit", importWorkbook);
  connectRealtime();
});

async function loadDashboard() {
  const [summary, chartData, posts] = await Promise.all([
    requestJson("/api/summary"), requestJson("/api/chart-data"), requestJson("/api/posts")
  ]);
  if (summary && chartData && posts) {
    renderSummary(summary);
    renderChart(chartData);
    renderPosts(posts);
  }
  document.getElementById("dashboard").setAttribute("aria-busy", "false");
}

async function refreshDashboard() {
  const button = document.getElementById("refresh-button");
  button.disabled = true;
  button.textContent = "Refreshing…";
  await loadDashboard();
  button.disabled = false;
  button.textContent = "Refresh data";
}

async function requestJson(url) {
  try {
    const response = await fetch(url);
    if (!response.ok) throw new Error(`Request failed: ${response.status}`);
    return await response.json();
  } catch (error) {
    showError(url.includes("posts") ? "posts-state" : "chart-state", "Could not load data. Try again.");
    return null;
  }
}

function renderSummary(summary) {
  document.getElementById("total-posts").textContent = formatNumber(summary.totalPosts);
  document.getElementById("total-likes").textContent = formatNumber(summary.totalLikes);
  document.getElementById("total-shares").textContent = formatNumber(summary.totalShares);
  document.getElementById("total-followers").textContent = formatNumber(summary.totalFollowers);
  document.getElementById("last-updated").textContent = summary.lastUpdated ? `Updated ${new Date(summary.lastUpdated).toLocaleString()}` : "No metrics yet";
}

function renderChart(points) {
  const state = document.getElementById("chart-state");
  if (!points.length) { state.textContent = "No metrics yet. Import data or add a metric."; return; }
  state.textContent = "";
  const labels = points.map(point => new Date(point.measuredAt).toLocaleDateString());
  const datasets = ["likes", "shares", "comments"].map((key, index) => ({
    label: key[0].toUpperCase() + key.slice(1), data: points.map(point => point[key]),
    borderColor: ["#1e40af", "#3b82f6", "#d97706"][index], backgroundColor: "transparent", tension: 0.25
  }));
  chart?.destroy();
  chart = new Chart(document.getElementById("metrics-chart"), { type: "line", data: { labels, datasets }, options: { responsive: true, maintainAspectRatio: false, plugins: { legend: { position: "bottom" } } } });
}

function renderPosts(posts) {
  const body = document.getElementById("posts-body");
  const state = document.getElementById("posts-state");
  body.innerHTML = posts.slice(0, 10).map(post => `<tr><td>${escapeHtml(post.content)}</td><td>${post.source}</td><td>${new Date(post.publishedAt).toLocaleDateString()}</td></tr>`).join("");
  state.textContent = posts.length ? "" : "No posts yet. Import a workbook to begin.";
}

async function importWorkbook(event) {
  event.preventDefault();
  const state = document.getElementById("import-state");
  const data = new FormData(event.target);
  state.textContent = "Uploading…";
  try {
    const csrfResponse = await fetch("/api/csrf");
    if (!csrfResponse.ok) throw new Error("CSRF token request failed");
    const csrf = await csrfResponse.json();
    const response = await fetch("/api/import-posts", { method: "POST", body: data,
      headers: { "X-XSRF-TOKEN": csrf.token } });
    if (!response.ok) throw new Error("Import failed");
    const result = await response.json();
    state.textContent = `Imported ${result.importedRows}, rejected ${result.rejectedRows}.`;
    await loadDashboard();
  } catch (error) { showError("import-state", "Import failed. Check the workbook and try again."); }
}

function connectRealtime() {
  if (!window.SockJS || !window.Stomp) return;
  const client = Stomp.over(new SockJS("/ws"));
  client.debug = null;
  client.connect({}, () => { document.getElementById("live-status").textContent = "Live"; client.subscribe("/topic/metrics", loadDashboard); }, () => { document.getElementById("live-status").textContent = "Polling"; setTimeout(connectRealtime, 5000); });
}

function formatNumber(value) { return new Intl.NumberFormat().format(value ?? 0); }
function showError(id, message) { const element = document.getElementById(id); element.textContent = message; element.classList.add("error"); }
function escapeHtml(value) { return String(value).replace(/[&<>'"]/g, character => ({ "&": "&amp;", "<": "&lt;", ">": "&gt;", "'": "&#39;", '"': "&quot;" }[character])); }
