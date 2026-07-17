import { useState } from 'react'
import type { FormEvent } from 'react'
import type { SubmitTaskRequest, Urgency } from '../api/types'

interface Props {
  onSubmit: (request: SubmitTaskRequest) => Promise<void>
}

const URGENCIES: Urgency[] = ['LOW', 'MEDIUM', 'HIGH', 'CRITICAL']

export function TaskForm({ onSubmit }: Props) {
  const [title, setTitle] = useState('')
  const [urgency, setUrgency] = useState<Urgency>('MEDIUM')
  const [effort, setEffort] = useState(3)
  const [busy, setBusy] = useState(false)

  async function handleSubmit(event: FormEvent) {
    event.preventDefault()
    setBusy(true)
    try {
      await onSubmit({ title: title.trim(), urgency, effort })
      setTitle('')
      setUrgency('MEDIUM')
      setEffort(3)
    } finally {
      setBusy(false)
    }
  }

  return (
    <form className="card form" onSubmit={handleSubmit}>
      <h2>Enviar tarea</h2>
      <label>
        Título
        <input
          value={title}
          onChange={(e) => setTitle(e.target.value)}
          placeholder="Configurar CI"
          required
        />
      </label>
      <label>
        Urgencia
        <select value={urgency} onChange={(e) => setUrgency(e.target.value as Urgency)}>
          {URGENCIES.map((u) => (
            <option key={u} value={u}>
              {u}
            </option>
          ))}
        </select>
      </label>
      <label>
        Esfuerzo (puntos)
        <input
          type="number"
          min={1}
          max={13}
          value={effort}
          onChange={(e) => setEffort(Number(e.target.value))}
          required
        />
      </label>
      <button type="submit" disabled={busy || title.trim() === ''}>
        {busy ? 'Asignando…' : 'Asignar automáticamente'}
      </button>
    </form>
  )
}
