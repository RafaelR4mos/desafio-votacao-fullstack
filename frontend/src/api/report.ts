import { api } from "@/lib/axios";
import type { RelatorioVote } from "@/model/Report";
import type { AxiosResponse } from "axios";

const ENDPOINT_PREFIX = "/report";

export async function getReportVote(): Promise<AxiosResponse<RelatorioVote>> {
  return await api.get(`${ENDPOINT_PREFIX}`);
}
