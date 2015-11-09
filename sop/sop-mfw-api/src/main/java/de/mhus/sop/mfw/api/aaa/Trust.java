package de.mhus.sop.mfw.api.aaa;

import java.util.List;
import java.util.UUID;

public interface Trust {


	public String getTrust();

	public boolean isValide();

	public boolean validatePassword(String password);

	public Ace findGlobalAce(String key);

	public Ace findAce(String type, UUID id);

	public List<Ace> findGlobalAcesForAccount(String key);

	public List<Ace> findAcesForAccount(String type);

	public boolean isChanged();

}
