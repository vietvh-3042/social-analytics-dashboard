import Link from "next/link";
import { BarChart3, LayoutDashboard } from "lucide-react";
import { APPLICATION_NAME, DASHBOARD_SECTIONS } from "@/shared/config/constants";

export const DashboardSidebar = () => (
  <aside className="bg-sidebar hidden w-64 shrink-0 border-r px-4 py-6 md:flex md:flex-col">
    <Link className="flex items-center gap-3 px-2 font-semibold tracking-tight" href="/dashboard">
      <span className="bg-primary text-primary-foreground grid size-10 place-items-center rounded-xl">
        <BarChart3 aria-hidden="true" className="size-5" />
      </span>
      <span>{APPLICATION_NAME}</span>
    </Link>
    <p className="text-muted-foreground mt-10 mb-3 px-3 font-mono text-[10px] tracking-[0.2em] uppercase">
      Workspace
    </p>
    <nav aria-label="Dashboard navigation" className="space-y-1">
      <a
        aria-current="page"
        className="bg-sidebar-accent text-primary focus-visible:ring-ring flex min-h-11 items-center gap-3 rounded-lg px-3 text-sm font-medium focus-visible:ring-2 focus-visible:outline-none"
        href={`#${DASHBOARD_SECTIONS.overview.id}`}
      >
        <LayoutDashboard aria-hidden="true" className="size-[18px]" />
        {DASHBOARD_SECTIONS.overview.label}
      </a>
    </nav>
    <div className="mt-auto rounded-xl border bg-slate-50 p-4">
      <p className="text-xs font-semibold">Synthetic metrics</p>
      <p className="text-muted-foreground mt-1 text-xs leading-5">
        Generated values are for demonstration. Facebook metrics are not fetched.
      </p>
    </div>
  </aside>
);
