package br.com.srm.wisedelivery.dominio;

import jakarta.persistence.Entity;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@MappedSuperclass

public class Cliente extends Usuario {

    
    
    @Getter @Setter
    private String cpf;
    
}