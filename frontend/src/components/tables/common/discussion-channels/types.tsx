import { ErrorType, GenericResponse } from '../../../../common/types';

export class CommonDiscussionChannel {
  id?: number;
  name?: string;
  created_at?: string;
  created_by?: number;
  modified_at?: string;
  modified_by?: number;
  owning_person?: number;
  owning_group?: number;
}

export class CreateCommonDiscussionChannelRequest {
  token: string;
  discussion_channel: {
    name: string;
  };
}

export class CreateCommonDiscussionChannelResponse extends GenericResponse {
  discussion_channel: CommonDiscussionChannel;
}

export class CommonDiscussionChannelListRequest {
  token: string;
}

export class CommonDiscussionChannelListResponse {
  error: ErrorType;
  discussion_channels: CommonDiscussionChannel[];
}

export class CommonDiscussionChannelUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: number;
}

export class CommonDiscussionChannelUpdateResponse {
  error: ErrorType;
  discussion_channel: CommonDiscussionChannel | null = null;
}

export class DeleteCommonDiscussionChannelRequest {
  id: number;
  token: string;
}

export class DeleteCommonDiscussionChannelResponse extends GenericResponse {
  id: number;
}
