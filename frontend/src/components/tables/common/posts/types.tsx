import { ErrorType, GenericResponse } from '../../../../common/types';

export class CommonPost {
  id?: string | undefined;
  content?: string | undefined;
  thread?: number | undefined;
  created_at?: string | undefined;
  created_by?: number | undefined;
  modified_at?: string | undefined;
  modified_by?: number | undefined;
  owning_person?: number | undefined;
  owning_group?: number | undefined;
}

export class CreateCommonPostsRequest {
  token: string;
  post: {
    content: string;
    thread: string;
  };
}

export class CreateCommonPostsResponse extends GenericResponse {
  post: CommonPost;
}

export class CommonPostsListRequest {
  token: string;
  threadId?: string;
}

export class CommonPostsListResponse {
  error: ErrorType;
  posts: CommonPost[];
}

export class CommonPostsUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: string;
}

export class CommonPostsUpdateResponse {
  error: ErrorType;
  post: CommonPost | null = null;
}

export class DeleteCommonPostsRequest {
  id: string;
  token: string;
}

export class DeleteCommonPostsResponse extends GenericResponse {
  id: string;
}
