import { api } from "@/lib/axios";
import type {
  Vote,
  VoteCheck,
  VoteCheckResponse,
  VoteCreate,
  VoteResultsResponse,
} from "@/model/Votes";
import type { AxiosResponse } from "axios";

const ENDPOINT_PREFIX = "/votes";

export async function vote({
  idAssociate,
  idSession,
  vote,
}: VoteCreate): Promise<AxiosResponse<Vote>> {
  return await api.post(
    `${ENDPOINT_PREFIX}/session/${idSession}/associate/${idAssociate}/vote`,
    {
      vote,
    },
  );
}

export async function checkIfAssociateAlreadyVoted({
  idAssociate,
  idSession,
}: VoteCheck): Promise<AxiosResponse<VoteCheckResponse>> {
  return await api.get(
    `${ENDPOINT_PREFIX}/session/${idSession}/associate/${idAssociate}/check`,
  );
}

export async function getVotesResult(
  idSession: string,
): Promise<AxiosResponse<VoteResultsResponse>> {
  return await api.get(`${ENDPOINT_PREFIX}/session/${idSession}/results`);
}
