import { ErrorType, GenericResponse } from '../../../../common/types';

export class CommonDiscussionChannel {
  id: number;
  name: string;
  created_at: string;
  created_by: number;
  modified_at: string;
  modified_by: number;
  owning_person: number;
  owning_group: number;
}

export class CreateDiscussionChannelRequest {
  token: string;
  discussion_channels: {
    name: string;
  };
}

export class CreateDiscussionChannelResponse extends GenericResponse {
  discussion_channels: CommonDiscussionChannel;
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
  discussion_channels: CommonDiscussionChannel | null = null;
}

export class DeleteDiscussionChannelRequest {
  id: number;
  token: string;
}

export class DeleteDiscussionChannelResponse extends GenericResponse {
  id: number;
}
