import type { MemberResponse } from '../api/types'
import { MemberCard } from './MemberCard'

export function MemberBoard({ members }: { members: MemberResponse[] }) {
  if (members.length === 0) {
    return <p className="empty-board">Aún no hay miembros. Añade uno para empezar.</p>
  }

  return (
    <div className="board">
      {members.map((member) => (
        <MemberCard key={member.id} member={member} />
      ))}
    </div>
  )
}
