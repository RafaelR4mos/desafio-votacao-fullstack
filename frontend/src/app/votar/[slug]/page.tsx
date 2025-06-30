import { Voting } from "@/components/voting";

export default async function VotePage({
  params,
}: {
  params: Promise<{ slug: string }>;
}) {
  const { slug } = await params;

  return <Voting slug={slug} />;
}
