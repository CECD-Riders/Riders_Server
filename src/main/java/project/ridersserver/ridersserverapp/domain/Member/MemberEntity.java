package project.ridersserver.ridersserverapp.domain.Member;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import javafx.scene.NodeBuilder;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;
import project.ridersserver.ridersserverapp.dto.MemberDto;

@SequenceGenerator(name = "MEMBER_SEQ_GENERATOR", sequenceName = "MEMBER_SEQ", initialValue = 1, allocationSize = 1)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "member")
public class MemberEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE , generator="MEMBER_SEQ_GENERATOR")
    private Long id;

    @Column(length = 20, nullable = false , unique = true)
    private String email;

    @Column(length = 100, nullable = false)
    private String password;

    public MemberDto toDTO(){
        return MemberDto.builder()
                .id(id)
                .email(email)
                .password(password)
                .build();
    }

    @Builder
    public MemberEntity(Long id, String email, String password) {
//        Assert.notNull(id,"id must not be null");
        Assert.hasText(email,"email must not be empty");
        Assert.hasText(password,"password must not be empty");
        this.id = id;
        this.email = email;
        this.password = password;
    }

}