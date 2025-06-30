import { api } from "@/lib/axios";
import type { Session } from "@/model/Session";
import type { AxiosResponse } from "axios";

const ENDPOINT_PREFIX = "/session";

export async function openSession({
  idSession,
  durationInSeconds,
}: {
  idSession: string;
  durationInSeconds: number;
}): Promise<AxiosResponse<Session>> {
  return await api.put(`${ENDPOINT_PREFIX}/${idSession}/start`, {
    durationInSeconds,
  });
}
