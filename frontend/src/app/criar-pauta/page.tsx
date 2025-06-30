"use client";

import { Header } from "@/components/header";
import { Button } from "@/components/ui/button";
import { ArrowLeft, FileText, Save } from "lucide-react";
import { useRouter } from "next/navigation";

import NextLink from "next/link";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { useForm, useWatch } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { createAgenda } from "@/api/agenda";
import { toast } from "sonner";
import type { GenerericErrorResponse } from "@/model/Error";
import type { AxiosError } from "axios";

const agendaCreateSchema = z.object({
  title: z
    .string({
      required_error: "O título da pauta é obrigatório",
      invalid_type_error: "O título da pauta deve ser uma string",
    })
    .nonempty("O título da pauta não pode ser vazio")
    .max(150, "O título da pauta deve ter no máximo 150 caracteres"),

  description: z
    .string()
    .min(20, "A descrição deve ter no mínimo 20 caracteres")
    .max(255, "A descrição deve ter no máximo 255 caracteres"),

  durationInSeconds: z.coerce
    .number({
      required_error: "A duração da sessao é obrigatória",
      invalid_type_error: "A duração deve ser um número",
    })
    .positive("A duração deve ser positiva e maior que zero")
    .max(86400, "A duração máxima permitida é de 24 horas"),
});

export default function CriarPauta() {
  const form = useForm<z.infer<typeof agendaCreateSchema>>({
    resolver: zodResolver(agendaCreateSchema),
    defaultValues: {
      title: "",
      description: "",
      durationInSeconds: 1,
    },
  });

  const formData = useWatch({
    control: form.control,
    defaultValue: form.getValues(),
  });

  const router = useRouter();

  const queryClient = useQueryClient();
  const { mutateAsync: doCreateAgenda, isPending } = useMutation({
    mutationFn: createAgenda,
  });

  async function handleCreateAgenda(
    formData: z.infer<typeof agendaCreateSchema>,
  ) {
    try {
      await doCreateAgenda({
        title: formData.title,
        description: formData.description,
        durationInSeconds: formData.durationInSeconds * 60,
      });

      toast.success("Sucesso ao criar pauta");
      queryClient.invalidateQueries({ queryKey: ["agendas"] });

      router.push("/");
    } catch (error) {
      const errorResponse = error as AxiosError<GenerericErrorResponse>;

      toast.error("Erro ao criar pauta", {
        description:
          errorResponse.response?.data.message ?? errorResponse.message,
      });
    }
  }

  return (
    <div className="min-h-screen bg-gray-50">
      <Header>
        <div className="flex items-center gap-3">
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
              Cadastrar Nova Pauta
            </h1>
            <p className="mt-1 text-gray-600">
              Preencha as informações da pauta para votação
            </p>
          </div>
        </div>
      </Header>
      <div className="mx-auto max-w-4xl p-4">
        <Card className="mt-6">
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <FileText className="h-5 w-5" />
              Informações da Pauta
            </CardTitle>
            <CardDescription>
              Defina o título, descrição e duração da nova pauta para votação.
            </CardDescription>
          </CardHeader>
          <CardContent>
            <Form {...form}>
              <form
                onSubmit={form.handleSubmit(handleCreateAgenda)}
                className="space-y-6"
              >
                {/* Título */}
                <div className="space-y-2">
                  <FormField
                    control={form.control}
                    name="title"
                    render={({ field }) => (
                      <FormItem>
                        <FormLabel>Título da Pauta *</FormLabel>
                        <FormControl>
                          <Input
                            {...field}
                            value={field.value}
                            placeholder="Ex. Aprovação do Orçamento Anual 2025"
                            ref={field.ref}
                          />
                        </FormControl>
                        <FormMessage />
                      </FormItem>
                    )}
                  />
                  <p className="text-sm text-gray-500">
                    Título claro e objetivo que identifica a pauta
                  </p>
                </div>

                {/* Descrição */}
                <div className="space-y-2">
                  <FormField
                    control={form.control}
                    name="description"
                    render={({ field }) => (
                      <FormItem>
                        <FormLabel>Descrição</FormLabel>
                        <FormControl>
                          <Textarea
                            {...field}
                            value={field.value}
                            placeholder="Descreva detalhadamente o que será votado, incluindo contexto, justificativas e impactos da decisão..."
                            ref={field.ref}
                          />
                        </FormControl>
                        <FormMessage />
                      </FormItem>
                    )}
                  />
                  <p className="text-sm text-gray-500">
                    Forneça informações suficientes para que os associados
                    possam tomar uma decisão informada
                  </p>
                </div>

                {/* Duração */}
                <FormField
                  control={form.control}
                  name="durationInSeconds"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>Duração da Votação (minutos)</FormLabel>
                      <FormControl>
                        <Input
                          className="max-w-[200]"
                          type="number"
                          step={1}
                          {...field}
                        />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />
                <span className="text-sm text-gray-500">
                  Tempo que a sessão de votação ficará aberta (padrão: 1 minuto)
                </span>

                <div className="border-t pt-6">
                  <h2 className="mb-4 text-lg font-medium">Preview da Pauta</h2>
                  <Card className="bg-gray-50">
                    <CardHeader>
                      <CardTitle className="text-lg">
                        {formData.title || "Título da pauta aparecerá aqui"}
                      </CardTitle>
                      <CardDescription className="text-base">
                        {formData.description ||
                          "Descrição da pauta aparecerá aqui"}
                      </CardDescription>
                    </CardHeader>
                    <CardContent>
                      <div className="flex items-center gap-4 text-sm text-gray-500">
                        <span>
                          Duração: {formData.durationInSeconds} minuto(s)
                        </span>
                        <span>•</span>
                        <span>Status: Rascunho</span>
                      </div>
                    </CardContent>
                  </Card>
                </div>

                <div className="flex gap-4 border-t pt-6">
                  <NextLink href="/">
                    <Button
                      type="button"
                      variant="outline"
                      className="flex items-center gap-2 bg-transparent"
                    >
                      Cancelar
                    </Button>
                  </NextLink>
                  <Button
                    type="submit"
                    disabled={isPending}
                    className="flex items-center gap-2"
                  >
                    {isPending ? (
                      <>
                        <div className="h-4 w-4 animate-spin rounded-full border-2 border-white border-t-transparent" />
                        Salvando...
                      </>
                    ) : (
                      <>
                        <Save className="h-4 w-4" />
                        Criar Pauta
                      </>
                    )}
                  </Button>
                </div>
              </form>
            </Form>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}
