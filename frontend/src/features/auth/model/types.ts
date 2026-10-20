export type AuthUser = {
  id: string;
  name: string;
  email: string | null;
  pictureUrl: string | null;
};

export type CsrfResponse = {
  token: string;
};
