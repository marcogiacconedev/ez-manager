import { Input } from "@heroui/react"

function LoginCard(): React.ReactNode {
  const onSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    console.log("Login", e);
  }

  return (
    <form onSubmit={onSubmit} className="flex flex-col gap-4 w-full max-w-96">
      <div className="flex flex-col gap-1">
        <label htmlFor="nome">Nome</label>
        <Input id="nome" name="nome" placeholder="Mario Rossi" required />
      </div>
    </form>
  )
}

export default LoginCard;