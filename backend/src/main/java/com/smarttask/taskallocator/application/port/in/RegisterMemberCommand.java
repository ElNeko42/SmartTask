package com.smarttask.taskallocator.application.port.in;

/**
 * Datos necesarios para dar de alta a un miembro del equipo.
 */
public record RegisterMemberCommand(String name, int maxCapacity) {
}
