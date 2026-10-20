export type SocialPost = {
  id: string;
  title: string;
  postUrl: string;
  importedAt: string;
};

export type ImportRowResult = {
  rowNumber: number;
  status: 'ACCEPTED' | 'REJECTED';
  message: string;
};

export type ImportResult = {
  imported: number;
  rejected: number;
  rows: ImportRowResult[];
};
