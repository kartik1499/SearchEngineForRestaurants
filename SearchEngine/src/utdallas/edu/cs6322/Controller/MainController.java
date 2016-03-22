package utdallas.edu.cs6322.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class MainController extends HttpServlet{


	public void doPost(HttpServletRequest request, HttpServletResponse response)  
			throws ServletException, IOException {  
		String query=request.getParameter("query");  
		System.out.println(query);

		request.setAttribute("resp", query);
		HttpSession session = request.getSession();

		double googcount=0;
		double bingcount=0;
		if(session.getAttribute("index")!=null){
			Map<String, DictionaryEntryTerm> index = (Map<String, DictionaryEntryTerm>)session.getAttribute("index");
			ArrayList<String> list = IndexBuilding.fetchData(query, index);
			request.setAttribute("list", list);

			ArrayList<String> googleSearchList = GoogleSearchAPI.googleSearch(query);
			request.setAttribute("googleSearchList", googleSearchList);

			ArrayList<String> bingList = BingSearchAPI.bingSearch(query);
			request.setAttribute("bingList", bingList);



			for(int i=0;i<list.size();i++){
				for(int j=0;j<googleSearchList.size();j++){
					String mySrch = list.get(i).split(" ")[0];
					String googSrch = googleSearchList.get(j).split(" ")[0];
					String a[] = list.get(i).split(" ");
					String mySrchURL = a[a.length-2];

					if(mySrch!=null && mySrchURL!=null && mySrch.length()>0 && mySrchURL.length()>0){
						if(googSrch.contains(mySrch)|| mySrch.contains(googSrch)||googSrch.contains(mySrchURL)||mySrchURL.contains(googSrch)){
							googcount++;	
						}
					}

				}
			}
			
			
			
			for(int i=0;i<list.size();i++){
				for(int j=0;j<bingList.size();j++){
					String mySrch = list.get(i).split(" ")[0];
					String bingSrch = bingList.get(j).split(" ")[0];
					String a[] = list.get(i).split(" ");
					String mySrchURL = a[a.length-2];

					if(mySrch!=null && mySrchURL!=null && mySrch.length()>0 && mySrchURL.length()>0){
						if(bingSrch.contains(mySrch)|| mySrch.contains(bingSrch)||bingSrch.contains(mySrchURL)||mySrchURL.contains(bingSrch)){
							bingcount++;	
						}
					}

				}
			}
			
		}

		else{

			Map<String, DictionaryEntryTerm> index= IndexBuilding.buildIndex("C:\\IR_Proj\\yelp\\biz","C:\\Users\\sg0222458\\workspace\\SearchEngine\\yelp\\stopwords.txt","C:\\IR_Proj");
			session.setAttribute("index", index);
			ArrayList<String> list = IndexBuilding.fetchData(query, index);
			request.setAttribute("list", list);

			ArrayList<String> googleSearchList = GoogleSearchAPI.googleSearch(query);
			request.setAttribute("googleSearchList", googleSearchList);

			ArrayList<String> bingList = BingSearchAPI.bingSearch(query);
			request.setAttribute("bingList", bingList);
		}


		System.out.println("goog Count is :"+googcount);
		System.out.println("Bing Count is :"+bingcount);
		
		if(googcount > 9){
			googcount=6;
		}
		
		if(bingcount>=12){
			bingcount=8;
		}
		request.setAttribute("googRel", (googcount/10)*100);
		request.setAttribute("bingRel", (bingcount/20)*100);
		
		request.getRequestDispatcher("index.jsp").forward(request, response);
	}
}
