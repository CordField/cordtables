import { ErrorType, GenericResponse } from '../../../../common/types';

export class CommonThread {
  id?: string | undefined;
  content?: string | undefined;
  channel?: number | undefined;
  created_at?: string | undefined;
  created_by?: number | undefined;
  modified_at?: string | undefined;
  modified_by?: number | undefined;
  owning_person?: number | undefined;
  owning_group?: number | undefined;
}

export class CreateCommonThreadsRequest {
  token: string;
  thread: {
    content: string;
    channel: string;
  };
}

export class CreateCommonThreadsResponse extends GenericResponse {
  thread: CommonThread;
}

export class CommonThreadsListRequest {
  token: string;
  channelId?: string;
}

export class CommonThreadsListResponse {
  error: ErrorType;
  threads: CommonThread[];
}

export class CommonThreadsUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: string;
}

export class CommonThreadsUpdateResponse {
  error: ErrorType;
  thread: CommonThread | null = null;
}

export class DeleteCommonThreadsRequest {
  id: string;
  token: string;
}

export class DeleteCommonThreadsResponse extends GenericResponse {
  id: string;
}
