import { useState } from 'react'
import type { FormEvent } from 'react'
import type { RegisterMemberRequest } from '../api/types'

interface Props {
  onSubmit: (request: RegisterMemberRequest) => Promise<void>
}

export function MemberForm({ onSubmit }: Props) {
  const [name, setName] = useState('')
  const [maxCapacity, setMaxCapacity] = useState(8)
  const [busy, setBusy] = useState(false)

  async function handleSubmit(event: FormEvent) {
    event.preventDefault()
    setBusy(true)
    try {
      await onSubmit({ name: name.trim(), maxCapacity })
      setName('')
      setMaxCapacity(8)
    } finally {
      setBusy(false)
    }
  }

  return (
    <form className="card form" onSubmit={handleSubmit}>
      <h2>Nuevo miembro</h2>
      <label>
        Nombre
        <input
          value={name}
          onChange={(e) => setName(e.target.value)}
          placeholder="Ana"
          required
        />
      </label>
      <label>
        Capacidad máxima (puntos)
        <input
          type="number"
          min={1}
          value={maxCapacity}
          onChange={(e) => setMaxCapacity(Number(e.target.value))}
          required
        />
      </label>
      <button type="submit" disabled={busy || name.trim() === ''}>
        {busy ? 'Añadiendo…' : 'Añadir miembro'}
      </button>
    </form>
  )
}
