import type { Metadata } from "next";
import type { ReactNode } from "react";
import "@fontsource/fira-sans/400.css";
import "@fontsource/fira-sans/500.css";
import "@fontsource/fira-sans/600.css";
import "@fontsource/fira-sans/700.css";
import "@fontsource/fira-code/400.css";
import "@fontsource/fira-code/500.css";
import { Providers } from "@/providers/Providers";
import "./globals.css";

export const metadata: Metadata = {
  title: "Social Analytics Dashboard",
  description: "Synthetic Facebook post analytics for your workspace.",
};

const RootLayout = ({
  children,
}: Readonly<{
  children: ReactNode;
}>) => (
  <html lang="en">
    <body className="antialiased">
      <Providers>{children}</Providers>
    </body>
  </html>
);

export default RootLayout;
