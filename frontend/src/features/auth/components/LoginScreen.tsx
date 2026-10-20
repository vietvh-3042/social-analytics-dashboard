'use client';

import { useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { ArrowRight, BarChart3, Facebook, ShieldCheck } from 'lucide-react';
import { useCurrentUser } from '@/features/auth/hooks/useCurrentUser';
import { Button } from '@/components/ui/button';
import { APPLICATION_NAME, API_BASE_URL, API_PATHS } from '@/shared/config/constants';

export const LoginScreen = () => {
  const router = useRouter();
  const { data: user, isLoading } = useCurrentUser();

  useEffect(() => {
    if (user) router.replace('/dashboard');
  }, [router, user]);

  const signInWithFacebook = () => {
    window.location.assign(`${API_BASE_URL}${API_PATHS.oauthFacebook}`);
  };

  return (
    <main className="grid min-h-screen bg-white lg:grid-cols-[1.05fr_0.95fr]">
      <section className="relative hidden overflow-hidden bg-primary px-12 py-10 text-primary-foreground lg:flex lg:flex-col lg:justify-between xl:px-20">
        <div className="relative z-10 flex items-center gap-3 font-semibold tracking-tight">
          <span className="grid size-10 place-items-center rounded-xl bg-white/15">
            <BarChart3 aria-hidden="true" className="size-5" />
          </span>
          {APPLICATION_NAME}
        </div>
        <div className="relative z-10 max-w-xl pb-16">
          <p className="mb-5 font-mono text-xs uppercase tracking-[0.24em] text-blue-100">Your social data, in focus</p>
          <h1 className="text-5xl font-semibold leading-[1.12] tracking-tight xl:text-6xl">
            Make every post easier to understand.
          </h1>
          <p className="mt-6 max-w-lg text-lg leading-8 text-blue-100">
            Follow engagement trends, set a refresh rhythm, and keep your Facebook posts organized in one calm workspace.
          </p>
        </div>
        <div className="relative z-10 flex items-center gap-2 text-sm text-blue-100">
          <ShieldCheck aria-hidden="true" className="size-4" />
          Your workspace is private to your Facebook account.
        </div>
        <div aria-hidden="true" className="absolute -right-36 top-1/4 size-[34rem] rounded-full border border-white/10 bg-white/5" />
        <div aria-hidden="true" className="absolute -bottom-64 -left-32 size-[36rem] rounded-full border border-white/10" />
      </section>

      <section className="flex min-h-screen flex-col justify-between px-6 py-8 sm:px-12 lg:px-16">
        <div className="flex items-center gap-3 font-semibold tracking-tight lg:hidden">
          <span className="grid size-10 place-items-center rounded-xl bg-primary text-white">
            <BarChart3 aria-hidden="true" className="size-5" />
          </span>
          {APPLICATION_NAME}
        </div>
        <div className="mx-auto flex w-full max-w-md flex-1 flex-col justify-center py-12">
          <p className="font-mono text-xs uppercase tracking-[0.2em] text-muted-foreground">Admin workspace</p>
          <h2 className="mt-3 text-3xl font-semibold tracking-tight sm:text-4xl">Welcome back</h2>
          <p className="mt-3 text-muted-foreground">Sign in to manage your posts and review engagement history.</p>
          <Button className="mt-9 h-12 w-full gap-3 text-base" onClick={signInWithFacebook} disabled={isLoading}>
            <Facebook aria-hidden="true" className="size-5" />
            Continue with Facebook
            <ArrowRight aria-hidden="true" className="ml-auto size-4" />
          </Button>
          <div className="mt-5 rounded-xl border bg-slate-50 p-4 text-sm leading-6 text-muted-foreground">
            Facebook is the only sign-in method. No password account or X connection is available.
          </div>
          <p className="mt-7 text-center text-xs leading-5 text-muted-foreground">
            Engagement values are generated for demonstration and analysis. They are not retrieved from Facebook.
          </p>
        </div>
        <footer className="text-center text-xs text-muted-foreground">{APPLICATION_NAME} · Private analytics workspace</footer>
      </section>
    </main>
  );
};
