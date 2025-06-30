import { api } from "@/lib/axios";
import type { Associate, AssociateStats } from "@/model/Associate";
import type { AxiosResponse } from "axios";

const ENDPOINT_PREFIX = "/associate";

export async function findAssociateByCpf({
  cpf,
}: {
  cpf: string;
}): Promise<AxiosResponse<Associate>> {
  return await api.get(`${ENDPOINT_PREFIX}/by-cpf/${cpf}`);
}

export async function getCountAllAssociate(): Promise<
  AxiosResponse<AssociateStats>
> {
  return await api.get(`${ENDPOINT_PREFIX}/count-all`);
}
