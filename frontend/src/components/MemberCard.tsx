import type { MemberResponse } from '../api/types'

export function MemberCard({ member }: { member: MemberResponse }) {
  const percent =
    member.maxCapacity === 0
      ? 0
      : Math.round((member.currentLoad / member.maxCapacity) * 100)
  const full = member.currentLoad >= member.maxCapacity

  return (
    <div className="card member">
      <header className="member-header">
        <h3>{member.name}</h3>
        <span className="load">
          {member.currentLoad}/{member.maxCapacity} pts
        </span>
      </header>

      <div className="bar" title={`${percent}% ocupado`}>
        <div className="bar-fill" data-full={full} style={{ width: `${percent}%` }} />
      </div>

      <ul className="tasks">
        {member.tasks.length === 0 && <li className="empty">Sin tareas asignadas</li>}
        {member.tasks.map((task) => (
          <li key={task.id} className="task">
            <span className={`badge urgency-${task.urgency.toLowerCase()}`}>{task.urgency}</span>
            <span className="task-title">{task.title}</span>
            <span className="effort">{task.effort} pts</span>
          </li>
        ))}
      </ul>
    </div>
  )
}
