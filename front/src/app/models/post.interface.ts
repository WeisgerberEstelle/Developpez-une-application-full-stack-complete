export interface Post {
  id: number;
  title: string;
  content: string;
  author: { id: number; username: string };
  topic: { id: number; name: string };
  createdAt: string;
}

export interface CreatePostRequest {
  topicId: number;
  title: string;
  content: string;
}
