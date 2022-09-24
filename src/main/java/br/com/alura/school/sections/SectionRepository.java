package br.com.alura.school.sections;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface SectionRepository extends JpaRepository<Section, Long> {
    Optional<Section> findByCode(String code);
}