package rampoo;

import java.util.Arrays;

import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RealmRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

public class KeycloakAdminApp {

	public static void main(String[] args) {

		Keycloak keycloak = KeycloakBuilder.builder()
		        .serverUrl("http://localhost:8080")
		        .grantType(OAuth2Constants.PASSWORD)
		        .realm("master")
		        .clientId("admin-cli")
		        .username("pramaswamy")
		        .password("Rampoo@1981")
		        .resteasyClient(
		            new ResteasyClientBuilderImpl()
		                .connectionPoolSize(10).build()
		        ).build();
		
		
		RealmRepresentation rr = new RealmRepresentation();
		rr.setId("kafkaui");
		rr.setRealm("kafkaui");
		rr.setEnabled(true);

		keycloak.realms().create(rr);

		
		RoleRepresentation role=new RoleRepresentation();
		role.setName("Admin");
		keycloak.realm("kafkaui").roles().create(role);
		role=new RoleRepresentation();
		role.setName("Developer");
		keycloak.realm("kafkaui").roles().create(role);

		CredentialRepresentation credential = new CredentialRepresentation();
		credential.setType(CredentialRepresentation.PASSWORD);
		credential.setValue("pass@123");
		UserRepresentation user = new UserRepresentation();
		user.setEnabled(true);
		user.setUsername("admin");
		user.setFirstName("Admin");
		user.setLastName("auser");
		user.setEmail("adminuser1@verizon.com");
		user.setCredentials(Arrays.asList(credential));
		user.setRealmRoles(Arrays.asList("Admin"));
		keycloak.realm("kafkaui").users().create(user);
		
		credential.setType(CredentialRepresentation.PASSWORD);
		credential.setValue("pass@123");
		user = new UserRepresentation();
		user.setEnabled(true);
		user.setUsername("developer");
		user.setFirstName("Developer");
		user.setLastName("duser");
		user.setEmail("developeruser1@verizon.com");
		user.setCredentials(Arrays.asList(credential));
		keycloak.realm("kafkaui").users().create(user);

        ClientRepresentation clientRepresentation = new ClientRepresentation();
        clientRepresentation.setClientId("kafkaui");
        clientRepresentation.setName("kafkaui");
        clientRepresentation.setPublicClient(true);
        clientRepresentation.setEnabled(true);
        clientRepresentation.setProtocol("openid-connect");
        clientRepresentation.setSecret("kafkaui-secret");
        keycloak.realm("kafkaui").clients().create(clientRepresentation);


	}
}
