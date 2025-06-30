import { cn } from "@/lib/utils";

export type HeaderProps = {
  children: React.ReactNode;
  classname?: string;
};

export function Header({ children, classname }: HeaderProps) {
  return (
    <header className="border-b bg-white">
      <div
        className={cn(
          "m-auto flex max-w-[100%] flex-wrap items-center justify-between gap-2 px-4 py-4 md:max-w-[80%]",
          classname,
        )}
      >
        {children}
      </div>
    </header>
  );
}
