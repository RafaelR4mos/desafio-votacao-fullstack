"use client";

import { findBySlug } from "@/api/agenda";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { useEffect, useState } from "react";
import { toast } from "sonner";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "./ui/card";
import {
  AlertTriangle,
  ArrowLeft,
  CheckCircle,
  Clock,
  FileText,
  Loader2,
  User,
  Vote,
  XCircle,
} from "lucide-react";
import { Button } from "./ui/button";

import NextLink from "next/link";
import { Header } from "./header";
import { Badge } from "./ui/badge";
import {
  formatDate,
  formatSecondsToMinutes,
  formatTime,
} from "@/utils/dateFormaters";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "./ui/form";
import { z } from "zod";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { findAssociateByCpf } from "@/api/associate";
import { Input } from "./ui/input";
import { InputMask } from "@react-input/mask";

import { Skeleton } from "./ui/skeleton";
import type { TypeVote } from "@/model/Votes";
import { checkIfAssociateAlreadyVoted, vote } from "@/api/votes";
import type { AxiosError } from "axios";
import type { GenerericErrorResponse } from "@/model/Error";
import { useRouter } from "next/navigation";

type VotingProps = {
  slug: string;
};

const cpfSchema = z.object({
  cpf: z
    .string()
    .nonempty("CPF é obrigatório")
    .refine(
      (val) => {
        const cpf = val.replace(/[^\d]+/g, "");
        if (cpf.length !== 11 || /^(\d)\1{10}$/.test(cpf)) {
          return false;
        }
        return true;
      },
      {
        message: "CPF inválido",
      },
    ),
});

export function Voting({ slug }: VotingProps) {
  const [confirmIdentity, setConfirmIdentity] = useState(false);
  const [selectedVote, setSelectedVote] = useState<TypeVote | null>(null);
  const router = useRouter();

  const form = useForm<z.infer<typeof cpfSchema>>({
    resolver: zodResolver(cpfSchema),
    defaultValues: {
      cpf: "",
    },
  });

  const [tempoRestante, setTempoRestante] = useState(0);
  const [isTimeEnded, setTimeEnded] = useState(false);

  const queryClient = useQueryClient();
  const { data: agendaData, isLoading: isLoadingAgenda } = useQuery({
    queryFn: async () => {
      return (await findBySlug(slug)).data;
    },
    queryKey: ["agenda-by-slug", slug],
  });

  const {
    data: associateData,
    isLoading: isLoadingAssociate,
    isError: isErrorAssociate,
    refetch,
  } = useQuery({
    queryFn: async () => {
      const { cpf } = form.getValues();
      return await findAssociateByCpf({ cpf: cpf.replace(/\D/g, "") });
    },
    queryKey: ["associate"],
    enabled: false,
  });

  const { data: checkVoteData, refetch: refetchCheckVote } = useQuery({
    queryFn: async () => {
      if (associateData?.data.idAssociate && agendaData?.session.idSession) {
        return await checkIfAssociateAlreadyVoted({
          idAssociate: associateData?.data.idAssociate,
          idSession: agendaData?.session.idSession,
        });
      }

      return Promise.resolve(null);
    },
    queryKey: [
      "check-vote",
      associateData?.data.idAssociate,
      agendaData?.session.idSession,
    ],
    enabled:
      !!associateData?.data.idAssociate && !!agendaData?.session.idSession,
  });

  const { mutateAsync: doVote, isPending: confirmandoVoto } = useMutation({
    mutationFn: vote,
    onSuccess() {
      toast.success("Seu voto foi registrado!", {
        description: "Seu voto foi contabilizado e não poderá ser desfeito.",
      });

      router.push("/");
    },
    onError(err) {
      const errorFormated = err as AxiosError<GenerericErrorResponse>;
      const message =
        errorFormated.response?.data?.message ?? "Erro inesperado ao votar";

      toast.error("Falha ao contabilizar voto", {
        description: message,
      });
    },
  });

  async function handleFindAssociate() {
    queryClient.removeQueries({ queryKey: ["associate"] });
    refetch();
  }

  useEffect(() => {
    if (agendaData && agendaData.session.status === "VOTING" && !isTimeEnded) {
      const interval = setInterval(() => {
        const now = new Date();
        const tempoRestanteMs =
          new Date(agendaData.session.finishedAt).getTime() - now.getTime();

        if (tempoRestanteMs > 0) {
          const novoTempo = Math.ceil(tempoRestanteMs / 1000);
          setTempoRestante(novoTempo);
        } else {
          setTimeEnded(true); // marca que acabou
          setTempoRestante(0); // evita números negativos
          clearInterval(interval); // limpa o intervalo

          toast.info(`Sessão Finalizada`, {
            description: `A votação da pauta ${agendaData.title} foi encerrada!`,
          });
        }
      }, 1000);

      return () => clearInterval(interval);
    }
  }, [agendaData, isTimeEnded]);

  useEffect(() => {
    return () => {
      queryClient.removeQueries({ queryKey: ["associate"] });
    };
  }, [queryClient]);

  useEffect(() => {
    refetchCheckVote();
  }, [confirmIdentity, refetchCheckVote]);

  if (agendaData?.session.status !== "VOTING") {
    return (
      <div className="flex min-h-screen items-center justify-center bg-gray-50">
        <Card className="w-full max-w-[100%] px-4 md:max-w-md">
          <CardContent className="py-8 text-center">
            <AlertTriangle className="mx-auto mb-4 h-12 w-12 text-yellow-500" />
            <h2 className="mb-2 text-xl font-semibold">
              Votação Não Disponível
            </h2>
            <p className="mb-4 text-gray-600">
              Esta pauta não está em período de votação.
            </p>
            <NextLink href="/">
              <Button>Voltar ao Início</Button>
            </NextLink>
          </CardContent>
        </Card>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50 pb-4">
      <Header classname="max-w-[100%] px-4 md:max-w-[60%]">
        <div className="flex flex-wrap items-center justify-start gap-3">
          <NextLink href="/">
            <Button
              variant="ghost"
              size="sm"
              className="flex items-center gap-2"
            >
              <ArrowLeft className="h-4 w-4" />
              Voltar
            </Button>
          </NextLink>
          <div>
            <h1 className="text-3xl font-bold text-gray-900">
              Participar da Votação
            </h1>
            <p className="mt-1 text-gray-600">
              Registre seu voto na pauta em discussão
            </p>
          </div>
          <Badge variant="default" className="text-sm">
            Votação Ativa
          </Badge>
        </div>
      </Header>

      {isLoadingAgenda && (
        <div className="m-auto mt-4 flex flex-col gap-4 md:max-w-md">
          <Skeleton className="h-16 w-full" />
          <Skeleton className="h-[200px] w-full" />
          <Skeleton className="h-[350px] w-full" />
        </div>
      )}
      <div className="m-auto mt-6 px-4 md:max-w-[60%]">
        {/* Timer para sessão ativa */}
        {agendaData.session.status === "VOTING" && (
          <div className="rounded-lg border border-blue-200 bg-blue-50 p-4">
            <div className="flex items-center justify-between">
              <div className="flex items-center gap-2">
                <Clock className="h-4 w-4 text-blue-600" />
                <span className="font-medium text-blue-900">
                  Tempo Restante
                </span>
              </div>
              <span className="font-mono text-2xl font-bold text-blue-600">
                {formatTime(tempoRestante)}
              </span>
            </div>
          </div>
        )}

        {/* Informações da Pauta */}
        <Card className="mt-4 overflow-hidden">
          <CardHeader>
            <div className="flex items-start gap-3">
              <FileText className="mt-1 h-6 w-6 text-gray-600" />
              <div className="flex-1">
                <CardTitle className="mb-2 text-xl">
                  {agendaData.title}
                </CardTitle>
                <CardDescription className="max-w-[300px] truncate text-base leading-relaxed md:max-w-[100%]">
                  {agendaData.description}
                </CardDescription>
                <div className="mt-3 flex flex-col items-start gap-4 text-sm text-gray-500 md:flex-row md:items-center">
                  <span>
                    Iniciada em: {formatDate(agendaData.session.startedAt)}
                  </span>
                  <span className="hidden md:block">•</span>
                  <span>
                    Encerra em: {formatDate(agendaData.session.finishedAt)}
                  </span>
                  <span className="hidden md:block">•</span>
                  <span>
                    Duração:{" "}
                    {formatSecondsToMinutes(
                      agendaData.session.durationInSeconds,
                    )}{" "}
                    minutos
                  </span>
                </div>
              </div>
            </div>
          </CardHeader>
        </Card>

        {/* Seleção de Associado */}
        <Card className="mt-4">
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <User className="h-5 w-5" />
              Identificação do Associado
            </CardTitle>
            <CardDescription>
              Preencha seus dados para se identificar
            </CardDescription>
          </CardHeader>
          <CardContent>
            <Form {...form}>
              <form
                onSubmit={form.handleSubmit(handleFindAssociate)}
                className="max-w-sm space-y-4 md:w-full"
              >
                <div className="flex flex-col items-end gap-2 md:flex-row">
                  <FormField
                    control={form.control}
                    name="cpf"
                    render={({ field }) => (
                      <FormItem className="w-full">
                        <FormLabel>Informe seu CPF:</FormLabel>
                        <FormControl>
                          <InputMask
                            mask="___.___.___-__"
                            replacement={{ _: /\d/ }}
                            {...field}
                            component={Input}
                          />
                        </FormControl>
                        <FormMessage />
                      </FormItem>
                    )}
                  />

                  <Button
                    type="submit"
                    className="w-full"
                    disabled={isLoadingAssociate}
                  >
                    {isLoadingAssociate ? (
                      <Loader2 className="animate-spin" />
                    ) : (
                      <span className="flex items-center gap-2">
                        <User /> Buscar associado
                      </span>
                    )}
                  </Button>
                </div>

                {isErrorAssociate && !associateData && !isLoadingAssociate && (
                  <p className="mt-3 text-sm text-red-600">
                    Nenhum associado encontrado com esse CPF.
                  </p>
                )}

                {isLoadingAssociate && (
                  <div className="mt-4">
                    <Skeleton className="mb-2 h-5 w-48" />
                  </div>
                )}
                {associateData && (
                  <>
                    <div className="mt-4 flex items-center gap-3 rounded-md border border-green-200 bg-green-50 px-4 py-3 text-sm text-green-800 shadow-sm">
                      <User className="h-4 w-4" />
                      <span>
                        Associado encontrado:{" "}
                        <strong>
                          {associateData.data.firstName}{" "}
                          {associateData.data.lastName}
                        </strong>
                      </span>
                    </div>
                    <FormItem className="flex items-center space-x-2 space-y-0">
                      <FormControl>
                        <Input
                          type="checkbox"
                          className="h-5 w-5"
                          checked={confirmIdentity}
                          onChange={(e) => setConfirmIdentity(e.target.checked)}
                        />
                      </FormControl>
                      <div className="space-y-1 leading-none">
                        <FormLabel className="mt-1">
                          Confirmo que sou o titular do CPF informado
                        </FormLabel>
                        <FormMessage />
                      </div>
                    </FormItem>
                  </>
                )}
              </form>
            </Form>
          </CardContent>
        </Card>

        {associateData && (
          <Card
            className={`mt-4 border-2 border-blue-200 ${!confirmIdentity && "opacity-40"}`}
          >
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <Vote className="h-5 w-5" />
                Registrar Voto - {associateData.data.firstName}
                <p>{}</p>
              </CardTitle>
              <CardDescription>
                {checkVoteData?.data.alreadyVoted
                  ? "Você já registrou seu voto nesta pauta"
                  : "Selecione sua opção de voto e confirme"}
              </CardDescription>
            </CardHeader>
            <CardContent className="space-y-6">
              {checkVoteData?.data.alreadyVoted ? (
                <>
                  <div className="rounded-lg border border-green-200 bg-green-50 p-6 text-center">
                    <CheckCircle className="mx-auto mb-3 h-12 w-12 text-green-600" />
                    <h3 className="mb-2 text-lg font-semibold text-green-900">
                      Voto Já Registrado
                    </h3>
                    <p className="text-green-700">
                      Seu voto na pauta{" "}
                      <strong>{`"${agendaData.title}"`}</strong> foi registrado
                      com sucesso
                    </p>
                    <p className="mt-2 text-sm">
                      Voto realizado o dia:{" "}
                      <span className="font-semibold text-gray-700">
                        {formatDate(checkVoteData.data.votedAt)}
                      </span>
                    </p>

                    <p className="mt-1 text-sm text-gray-700">
                      Cada associado pode votar apenas uma vez por pauta.
                    </p>
                  </div>
                </>
              ) : (
                <div className="space-y-6">
                  {/* Opções de Voto */}
                  <div className="grid grid-cols-1 gap-4 md:grid-cols-2">
                    <button
                      disabled={!confirmIdentity}
                      onClick={() => setSelectedVote("SIM")}
                      className={`rounded-lg border-2 p-6 transition-all ${
                        selectedVote === "SIM"
                          ? "border-green-500 bg-green-50"
                          : "border-gray-200 hover:border-green-300 hover:bg-green-50 disabled:hover:border-gray-200 disabled:hover:bg-white"
                      } }`}
                    >
                      <div className="text-center">
                        <CheckCircle
                          className={`mx-auto mb-3 h-12 w-12 ${
                            selectedVote === "SIM"
                              ? "text-green-600"
                              : "text-gray-400"
                          }`}
                        />
                        <h3 className="text-xl font-bold text-green-700">
                          SIM
                        </h3>
                        <p className="mt-1 text-sm text-green-600">
                          Aprovar a proposta
                        </p>
                      </div>
                    </button>

                    <button
                      disabled={!confirmIdentity}
                      onClick={() => setSelectedVote("NAO")}
                      className={`rounded-lg border-2 p-6 transition-all ${
                        selectedVote === "NAO"
                          ? "border-red-500 bg-red-50"
                          : "border-gray-200 hover:border-red-300 hover:bg-red-50 disabled:hover:border-gray-200 disabled:hover:bg-white"
                      }`}
                    >
                      <div className="text-center">
                        <XCircle
                          className={`mx-auto mb-3 h-12 w-12 ${
                            selectedVote === "NAO"
                              ? "text-red-600"
                              : "text-gray-400"
                          }`}
                        />
                        <h3 className="text-xl font-bold text-red-700">NÃO</h3>
                        <p className="mt-1 text-sm text-red-600">
                          Rejeitar a proposta
                        </p>
                      </div>
                    </button>
                  </div>

                  {/* Confirmação */}
                  {selectedVote && (
                    <div className="rounded-lg border border-yellow-200 bg-yellow-50 p-4">
                      <div className="mb-3 flex items-center gap-3">
                        <AlertTriangle className="h-5 w-5 text-yellow-600" />
                        <h4 className="font-semibold text-yellow-900">
                          Confirmar Voto
                        </h4>
                      </div>
                      <p className="mb-4 text-yellow-800">
                        Você está prestes a votar{" "}
                        <strong>{selectedVote.toUpperCase()}</strong> na pauta{" "}
                        {`"${agendaData.title}`}.
                        <br />
                        <span className="text-sm text-yellow-700">
                          Esta ação não pode ser desfeita.
                        </span>
                      </p>
                      <div className="flex flex-col-reverse gap-3 md:flex-row">
                        <Button
                          onClick={() => setSelectedVote(null)}
                          variant="outline"
                          disabled={confirmandoVoto}
                        >
                          Cancelar
                        </Button>
                        <Button
                          onClick={() =>
                            doVote({
                              idAssociate: associateData.data.idAssociate,
                              idSession: agendaData.session.idSession,
                              vote: selectedVote,
                            })
                          }
                          disabled={confirmandoVoto}
                          className={
                            selectedVote === "SIM"
                              ? "bg-green-600 hover:bg-green-700"
                              : "bg-red-600 hover:bg-red-700"
                          }
                        >
                          {confirmandoVoto ? (
                            <>
                              <div className="mr-2 flex h-4 w-4 animate-spin items-center gap-2 rounded-full border-2 border-white border-t-transparent" />
                              <Loader2 className="animate-spin" />
                              Registrando Voto...
                            </>
                          ) : (
                            <span className="flex items-center gap-2">
                              {selectedVote === "NAO" ? (
                                <XCircle />
                              ) : (
                                <CheckCircle />
                              )}
                              Confirmar Voto {selectedVote.toUpperCase()}
                            </span>
                          )}
                        </Button>
                      </div>
                    </div>
                  )}
                </div>
              )}
            </CardContent>
          </Card>
        )}
      </div>
    </div>
  );
}
