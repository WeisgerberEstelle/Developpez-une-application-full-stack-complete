import { Topic } from './topic.interface';

export interface User {
  id: number;
  email: string;
  username: string;
  subscriptions: Topic[];
}

export interface UpdateProfileRequest {
  email?: string;
  username?: string;
  password?: string;
}
