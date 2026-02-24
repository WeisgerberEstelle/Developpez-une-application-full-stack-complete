export interface Comment {
  id: number;
  content: string;
  authorId: number;
  authorUsername: string;
  createdAt: string;
}

export interface CreateCommentRequest {
  content: string;
}
