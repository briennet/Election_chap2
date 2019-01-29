package istia.st.elections;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;


public class MainElections {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Scanner sc = new Scanner(System.in);
		
		boolean saisieOK = false;
		int nbSiegesAPouvoir=0;
		
		while(!saisieOK) {
			System.out.println("Veuillez saisir le nombre de sieges à pourvoir");
			
			try {	
				nbSiegesAPouvoir = sc.nextInt();	
				sc.nextLine();
				if(nbSiegesAPouvoir>0) saisieOK=true;
				else System.out.println("Nombre de sieges inferieur ou egal a 0");
			} catch(Exception InputMismatchException) {
				System.out.println("L\'entree est incorrecte (peut etre un nombre decimal ou une lettre? ) ");
				sc.nextLine();
			} 
		}
		
		saisieOK = false;
		int idListe=0;
		ArrayList<ListeElectorale> liste = new ArrayList<ListeElectorale>();
		int totalVoix=0;
		
		while(!saisieOK) {
			try {
			System.out.println("Veuillez saisir le nom de la liste: " + idListe + " (ou taper: \"*\" pour quitter l\'enregistrement des listes)");
			String nom = sc.nextLine();
			if(nom.equals("*")) break;
			System.out.println("Veuillez saisir le nombre de voix de la liste: " + nom);
			int nbVoix = sc.nextInt();
			sc.nextLine();
			totalVoix+=nbVoix;
			ListeElectorale tmp = new ListeElectorale(idListe, nom, nbVoix, 0, false,0); 
			liste.add(tmp);
			idListe++;
			} catch (ElectionsException ex) {
				System.out.println(ex.toString());
			} catch ( Exception InputMismatchException) {
				System.out.println("Une entree est incorrecte (peut etre une lettre plutot qu'un nombre pour le nombre de voix?) ");
				sc.nextLine();
			}
		}
		sc.close(); 
		
		if(liste.size() == 0 || totalVoix == 0) {
			System.out.println("aucune liste candidate");
			System.exit(0);
		}
		
		int nbVoixUtiles=0;
		for(int i=0;i<liste.size();i++) {
			double ratio = (double)liste.get(i).getVoix()/totalVoix;
			if(ratio<0.05) {
				liste.get(i).setElimine(true);
			}
			else nbVoixUtiles+=liste.get(i).getVoix();
		}
				
		if(nbVoixUtiles == 0) {
			System.out.println("aucune voix utile attribuée lors du vote");
			System.exit(0);
		}
		
		
		double quotienElectoral = (double)nbVoixUtiles/nbSiegesAPouvoir;
		int nbSiegePourvu = 0;

		for(int i=0;i<liste.size();i++) {
			if(!liste.get(i).isElimine()) {
				liste.get(i).setSieges((int)(liste.get(i).getVoix()/quotienElectoral));
				liste.get(i).setMoyenne((double)(liste.get(i).getVoix()/(liste.get(i).getSieges()+1)));
				nbSiegePourvu+=liste.get(i).getSieges();
			}
			else liste.get(i).setSieges(0);
		}
		
		for(int i=0;i<nbSiegesAPouvoir-nbSiegePourvu;i++) {
			double moyenneMax = liste.get(0).getMoyenne();
			int iMax=0;
			for(int j=1;j<liste.size();j++) {
				if(!liste.get(j).isElimine()) {
					if(liste.get(j).getMoyenne()>moyenneMax) {
						moyenneMax=liste.get(j).getMoyenne();
						iMax=j;
					}	
				}	
			}
			
			liste.get(iMax).setSieges(liste.get(iMax).getSieges()+1);
			liste.get(iMax).setMoyenne((double)liste.get(iMax).getVoix()/(liste.get(iMax).getSieges()+1));
		}
		
		liste.sort( new CompareListesElectorales());
		
		for(int i=0;i<liste.size();i++) {
			
			System.out.println("La liste "+liste.get(i).getNom()+" a obtenu " + liste.get(i).getSieges()+" siege(s)");
		}
		
		
		System.out.println("toto ");
	}
	
	

}

class CompareListesElectorales implements Comparator<ListeElectorale> {

	public int compare(ListeElectorale listeElectorale1,ListeElectorale listeElectorale2) {
		if(listeElectorale1.getSieges()==listeElectorale2.getSieges()) return 0;
		else if (listeElectorale1.getSieges()<listeElectorale2.getSieges()) return 1;
		else return -1;
		
	}
 }

