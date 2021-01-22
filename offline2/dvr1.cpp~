#include<bits/stdc++.h>
using namespace std;

class router
{
 private:
  char name[20];
  int dist[10];

 public:
  void get_r_table (int , int);
  friend void find_table (router *);
};

void find_table (router *);

int total_r;
int adj;
char names[10][20];
char N[20];

void router :: get_r_table (int no , int flag)
{
 if ( flag == 1 )
 {
  cout << "Enter the name of the adjacent router " << no << " : ";
  cin>>name;
 }
 else
  strcpy (name , N);

 cout << "Enter the distances of each router from this router :\n";
 cout << "\tROUTER\tDISTANCE\n";
 cout << "\t------\t--------\n";

 for (int i = 0 ; i < total_r ; i++)
 {
  cout << "\t" << names[i] << "\t";
  if ( strcmp ( names[i] , name ) == 0 )
  {
   dist[i] = 0;
   cout << dist[i] << "\n";
  }
  else
   cin >> dist[i];
 }
}

void find_table(router *r)
{
 cout << "Routing table for router " << N << " :-\n";
 cout << "\tRouter\tDistance\n";
 cout << "\t------\t--------\n";

 for ( int i = 0 ; i < total_r ; i++ )
 {
  int temp;
  if ( strcmp (N , names[i] ) == 0 )
   temp = 0;
  else
  {
   temp = r[0].dist[i];
   for ( int j = 1 ; j < adj + 1 ; j++ )
    if ( r[j].dist[i] < temp && r[j].dist[i] != 0 )
     temp = r[j].dist[i] + r[0].dist[j];
  }
  r[0].dist[i] = temp;
 // r[0] contains data of the router whose routing table is to be found out
 }

 for (int  i = 0 ; i < total_r ; i++ )
 {
  cout << "\t" << names[i] << "\t";
  cout << r[0].dist[i] << "\n";
 }
}

int main()
{
 router r[10];
 
 cout << "Enter the total no of routers in the subnet [MAX 10] :- ";
 cin >> total_r;
 cout << "Enter the names of all the routers :-\n";
 for (int i = 0 ; i < total_r ; i++)
 {
  cout << "\t" << i + 1 << " . ";
  cin>>names[i];
 }
 cout << "Enter the router whose routing table is to be found out : ";
 cin>>N;

 cout << "Enter the no of routers adjacent to this router : ";
 cin >> adj;

 int flag = 0;
 for (int i = 0 ; i < adj + 1; i++ )
 {
  r[i].get_r_table (i , flag);
  flag = 1;
 }

 find_table (r);
 return 0;
}
