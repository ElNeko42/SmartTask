package com.smarttask.taskallocator.infrastructure.adapter.out.persistence;

import com.smarttask.taskallocator.application.port.out.MemberRepository;
import com.smarttask.taskallocator.domain.model.member.Member;
import com.smarttask.taskallocator.domain.model.member.MemberId;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Adaptador de salida que implementa {@link MemberRepository} guardando los
 * agregados en memoria.
 *
 * <p>Sirve para arrancar el proyecto sin base de datos; al respetar el puerto,
 * podrá sustituirse por un adaptador JPA sin tocar dominio ni aplicación.
 */
@Repository
public class InMemoryMemberRepository implements MemberRepository {

    private final Map<MemberId, Member> store = new ConcurrentHashMap<>();

    @Override
    public Optional<Member> findById(MemberId id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public Member save(Member member) {
        store.put(member.id(), member);
        return member;
    }
}
