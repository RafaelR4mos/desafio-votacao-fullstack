import { useForm } from "react-hook-form";
import {
  Dialog,
  DialogDescription,
  DialogHeader,
  DialogTrigger,
  DialogContent,
  DialogTitle,
} from "./ui/dialog";
import { Button } from "./ui/button";
import {} from "@radix-ui/react-dialog";
import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import type { Session } from "@/model/Session";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { openSession } from "@/api/session";
import { toast } from "sonner";
import type { GenerericErrorResponse } from "@/model/Error";
import type { AxiosError } from "axios";
import { useState } from "react";
import {
  Form,
  FormControl,
  FormDescription,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "./ui/form";
import { Input } from "./ui/input";
import { Clock, Loader2, Play } from "lucide-react";

type OpenSessionDialogProps = {
  sessionData: Session;
  agendaTitle: string;
};

const openSessionSchema = z.object({
  durationInSeconds: z.coerce
    .number({
      required_error: "A duração da sessao é obrigatória",
      invalid_type_error: "A duração deve ser um número",
    })
    .positive("A duração deve ser positiva e maior que zero")
    .max(86400, "A duração máxima permitida é de 24 horas"),
});

export function OpenSessionDialog({
  sessionData,
  agendaTitle,
}: OpenSessionDialogProps) {
  const [isOpen, setIsOpen] = useState(false);
  const form = useForm<z.infer<typeof openSessionSchema>>({
    resolver: zodResolver(openSessionSchema),
    defaultValues: {
      durationInSeconds: Math.floor(sessionData.durationInSeconds / 60) || 1,
    },
  });

  const queryClient = useQueryClient();
  const { mutateAsync: doOpenSession, isPending } = useMutation({
    mutationFn: openSession,
  });

  async function handleOpenSession(
    formData: z.infer<typeof openSessionSchema>,
  ) {
    try {
      await doOpenSession({
        idSession: sessionData.idSession,
        durationInSeconds: formData.durationInSeconds * 60,
      });

      toast.success("Sucesso ao abrir sessão");
      queryClient.invalidateQueries({ queryKey: ["agendas"] });
      queryClient.invalidateQueries({ queryKey: ["relatorio"] });
      setIsOpen(false);
    } catch (error) {
      const errorResponse = error as AxiosError<GenerericErrorResponse>;
      toast.error("Erro ao abrir sessão", {
        description:
          errorResponse.response?.data.message ?? errorResponse.message,
      });
    }
  }

  return (
    <Dialog open={isOpen} onOpenChange={setIsOpen}>
      <DialogTrigger asChild>
        <Button className="flex items-center gap-2">
          <Play className="h-4 w-4" />
          Abrir Sessão de Votação
        </Button>
      </DialogTrigger>
      <Form {...form}>
        <DialogContent className="sm:max-w-[425px]">
          <form onSubmit={form.handleSubmit(handleOpenSession)}>
            <DialogHeader>
              <DialogTitle>Abrir sessão</DialogTitle>
            </DialogHeader>
            <DialogDescription>
              Configure a duração da sessão de votação para{" "}
              <span className="font-medium">{agendaTitle}</span>
            </DialogDescription>
            <div className="flex flex-col gap-2">
              <FormField
                control={form.control}
                name="durationInSeconds"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Duração da Votação (minutos)</FormLabel>
                    <FormControl>
                      <Input type="number" step={1} {...field} />
                    </FormControl>
                    <FormMessage />
                    <FormDescription>
                      Tempo que a votação ficará aberta para os associados
                    </FormDescription>
                  </FormItem>
                )}
              />

              <div className="mt-2 rounded-lg border border-blue-200 bg-blue-50 p-3">
                <div className="mb-2 flex items-center gap-2">
                  <Clock className="h-4 w-4 text-blue-600" />
                  <span className="text-sm font-medium text-blue-900">
                    Informações da Sessão
                  </span>
                </div>
                <div className="space-y-1 text-sm text-blue-800">
                  <p>• A votação será iniciada imediatamente</p>
                  <p>• Todos os associados poderão participar</p>
                  <p>• A sessão será encerrada automaticamente</p>
                </div>
              </div>
            </div>
            <div className="mt-4 flex w-full gap-2">
              <Button
                className="flex-1"
                variant="outline"
                onClick={() => setIsOpen(false)}
              >
                Cancelar
              </Button>
              <Button className="flex-1" type="submit" disabled={isPending}>
                {isPending ? (
                  <span className="flex items-center gap-1">
                    <Loader2 className="animate-spin" />
                    Iniciando...
                  </span>
                ) : (
                  <div className="flex items-center gap-2">
                    <Play />
                    Iniciar Votação
                  </div>
                )}
              </Button>
            </div>
          </form>
        </DialogContent>
      </Form>
    </Dialog>
  );
}
