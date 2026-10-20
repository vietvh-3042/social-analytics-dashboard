import { ExternalLink } from 'lucide-react';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '@/components/ui/table';
import type { SocialPost } from '@/features/posts/model/types';

type PostsTableProps = {
  posts: SocialPost[];
};

export const PostsTable = ({ posts }: PostsTableProps) => (
  <Card>
    <CardHeader className="pb-2">
      <CardTitle className="text-base">Tracked posts</CardTitle>
      <CardDescription>Facebook post references in your workspace.</CardDescription>
    </CardHeader>
    <CardContent>
      {posts.length === 0 ? (
        <div className="grid min-h-28 place-items-center text-center text-sm text-muted-foreground">
          Import an Excel workbook to start tracking your posts.
        </div>
      ) : (
        <div className="overflow-x-auto">
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Post</TableHead>
                <TableHead>Added</TableHead>
                <TableHead className="text-right">Open</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {posts.slice(0, 6).map((post) => (
                <TableRow key={post.id}>
                  <TableCell className="max-w-[18rem]">
                    <p className="truncate font-medium">{post.title}</p>
                    <p className="mt-1 truncate font-mono text-xs text-muted-foreground">{post.postUrl}</p>
                  </TableCell>
                  <TableCell className="whitespace-nowrap text-xs text-muted-foreground">
                    {new Date(post.importedAt).toLocaleDateString()}
                  </TableCell>
                  <TableCell className="text-right">
                    <a aria-label={`Open ${post.title} on Facebook`} className="inline-flex size-9 items-center justify-center rounded-md hover:bg-muted focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring" href={post.postUrl} rel="noreferrer" target="_blank">
                      <ExternalLink aria-hidden="true" className="size-4" />
                    </a>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </div>
      )}
    </CardContent>
  </Card>
);
