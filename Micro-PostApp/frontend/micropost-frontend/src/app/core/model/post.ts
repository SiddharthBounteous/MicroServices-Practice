export interface Post {
  postId: number;
  userId: number;
  username: string;
  content: string;
  createdAt: string;
  likeCount: number;
  commentCount: number;
  hasLiked: boolean;
}