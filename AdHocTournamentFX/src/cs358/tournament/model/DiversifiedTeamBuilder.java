package cs358.tournament.model;

import java.util.Collections;

public class DiversifiedTeamBuilder extends RandomTeamBuilder {

	public DiversifiedTeamBuilder() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
  protected void mix() {
		Collections.shuffle(mPlayingList);
		Collections.sort(mPlayingList, (a, b) -> a.getOrganization().compareToIgnoreCase(b.getOrganization()));
		/* for debugging
		System.out.println("Sorted by org");
		for (Participant player : mPlayingList) {
			System.out.println(player.getFirstName() + " " + player.getOrganization() );
		}
		*/
	}
	

}
