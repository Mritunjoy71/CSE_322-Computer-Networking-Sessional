#include<bits/stdc++.h>
#include <arpa/inet.h>
#include <sys/socket.h>
#include <unistd.h>

using namespace std;
struct node
{
    unsigned dist[10];
    unsigned from[10];
};
class Router
{

public :
    node DVR[10] ;
    int costmat[10][10];
    int nodes;

    Router(int nodes)
    {
        this->nodes=nodes;
        for(int i = 1; i <= nodes; i++)
        {
            for(int j = 1; j <= nodes; j++)
            {
                costmat[i][i] = 0;
                DVR[i].dist[j] = INT_MAX; //initialise the distance equal to -1
                costmat[i][j]=INT_MAX;
                DVR[i].from[j] =INT_MAX;

                DVR[j].dist[i] =INT_MAX; //initialise the distance equal to -1
                costmat[j][i]=INT_MAX;
                DVR[j].from[i] =INT_MAX;
            }
        }
    }
    void dvr_protocol()
    {
        for(int i = 1; i <= nodes; i++)  //We choose arbitary vertex k and we calculate the
        {
            //direct distance from the node i to k using the cost matrix and add the distance from k to node j
            for(int j = i+1; j <= nodes; j++)
            {
                for(int k = 1; k <= nodes; k++)
                    if(DVR[i].dist[j] > costmat[i][k] + DVR[k].dist[j])
                    {
                        //We calculate the minimum distance
                        DVR[i].dist[j] = DVR[i].dist[k] + DVR[k].dist[j];
                        DVR[j].dist[i] = DVR[i].dist[j];
                        DVR[i].from[j] = k;
                        DVR[j].from[i] = k;
                    }
            }
        }

    }

    void print()
    {
        for(int i = 1; i <= nodes; i++)
        {
            cout<<"\n\n For router: "<<i<<endl;
            cout<<"Destination\t\tNext Hop\t\tCost";

            for(int j = 1; j <= nodes; j++)
            {
                if(j==i)
                    ; //cout<<"\n--------\t------\t\t-------";
                else
                {
                    cout<<"\n192.168.10."<<j<<"\t\t";
                    if(DVR[i].from[j]==INT_MAX)
                        cout<<"--\t\t";
                    else
                        cout<<"192.168.10."<<DVR[i].from[j]<<"\t\t";
                    if(DVR[i].dist[j]==INT_MAX)
                        cout<<"\tinf";
                    else
                        cout<<DVR[i].dist[j];
                }


            }

            //cout<<"\n"<<j<<"\t\t"<<DVR[i].from[j]<<"\t\t"<<DVR[i].dist[j];
        }
    }

    void print_initial(int ip)
    {
        cout<<"\n\n Initial routing table For router: 192.168.10."<<ip<<endl;
        cout<<"Destination\t\tNext Hop\t\tCost";

        for(int j = 1; j <= nodes; j++)
        {
            if(j==ip)
                ; //cout<<"\n--------\t------\t\t-------";
            else
            {
                cout<<"\n192.168.10."<<j<<"\t\t";
                if(DVR[ip].from[j]==INT_MAX)
                    cout<<"--\t\t";
                else
                    cout<<"192.168.10."<<DVR[ip].from[j]<<"\t\t";
                if(DVR[ip].dist[j]==INT_MAX)
                    cout<<"\tinf";
                else
                    cout<<DVR[ip].dist[j];
            }


        }
    }


    void print_routing_table(int ip)
    {
        //dvr_protocol();
        cout<<"\n\n Routing table For router: 192.168.10."<<ip<<endl;
        cout<<"Destination\t\tNext Hop\t\tCost";

        for(int j = 1; j <= nodes; j++)
        {
            if(j==ip)
                ; //cout<<"\n--------\t------\t\t-------";
            else
            {
                cout<<"\n192.168.10."<<j<<"\t\t";
                if(DVR[ip].from[j]==INT_MAX)
                    cout<<"--\t\t";
                else
                    cout<<"192.168.10."<<DVR[ip].from[j]<<"\t\t";
                if(DVR[ip].dist[j]==INT_MAX)
                    cout<<"\tinf";
                else
                    cout<<DVR[ip].dist[j];
            }


        }
    }








};

int nodes=4;
Router *router=new Router(nodes);


int init_router(string argv1,string argv2)
{
    string arg1=argv1;
    stringstream ip(arg1);
    string temp;
    int count=0;
    int hipnum;

    while(getline(ip,temp, '.'))
    {
        count++;
        if(count==4)
        {
            stringstream ss(temp);
            ss>>hipnum;
        }
        // cout<<temp<<endl;

    }
    cout<<"host ip number 192.168.10."<<hipnum<<endl;
    string fname=argv2;
    // cout<<fname<<endl;

    fstream file;
    string word,filename;
    filename =fname;
    file.open(filename.c_str());
    int wc=0;
    int ip1,ip2,cost;
    while (file >> word)
    {
        wc++;
        if(wc==3)
        {
            wc=0;
            stringstream ss(word);
            ss>>cost;
            //cout<<ip1<<"\t"<<ip2<<"\t"<<cost<<endl;
            //g[ip1].push_back(make_pair(ip2,cost));
            //g[ip2].push_back(make_pair(ip1,cost));
            router->DVR[ip1].dist[ip2] =cost ;
            router->DVR[ip2].dist[ip1] =cost ;

            router->costmat[ip1][ip2]=cost;
            router->costmat[ip2][ip1]=cost;

            router->DVR[ip1].from[ip2] = ip2;
            router->DVR[ip2].from[ip1] = ip1;
        }

        if(wc==1)
        {
            stringstream w(word);
            count=0;
            while(getline(w,temp, '.'))
            {
                count++;
                if(count==4)
                {
                    stringstream ss(temp);
                    ss>>ip1;
                }
                // cout<<ip1<<endl;
            }
        }

        if(wc==2)
        {
            stringstream w(word);
            count=0;
            while(getline(w,temp, '.'))
            {
                count++;
                if(count==4)
                {
                    stringstream ss(temp);
                    ss>>ip2;
                }
                //cout<<ip2<<endl;
            }
        }
        // cout<<ip1<<"\t"<<ip2<<"\t"<<cost<<endl;

    }
    router->print_initial(hipnum);
    //router->dvr_protocol();
    //router->print();
    cout<<"\n\n";


    return hipnum;

}
int main(int argc, char *argv[])
{
    if(argc != 3)
    {
        printf("%s <ip address>\n", argv[0]);
        exit(1);
    }

    int hostip;
    hostip=init_router(argv[1],argv[2]);
    router->dvr_protocol();
///// driver er sathe communication


    int bytes_received;

    int sockfd;

    char buffer[1024];

    //	printf("Router's IP address: %s",IPAddress);

    int bind_flag;
    struct sockaddr_in client_address;

    client_address.sin_family = AF_INET;
    client_address.sin_port = htons(4747);
    inet_pton(AF_INET, argv[1], &client_address.sin_addr);

    sockfd = socket(AF_INET, SOCK_DGRAM, 0);

    bind_flag = bind(sockfd, (struct sockaddr*) &client_address, sizeof(sockaddr_in));

    if(!bind_flag)
        cout<<"Connection successful!!"<<endl;
    else
        cout<<"Connection failed!!!"<<endl;

    cout<<"--------------------------------------"<<endl;
    struct sockaddr_in router_address;
    socklen_t addrlen;

    //new addition

    char without_garbage[100];
    int counter = 0;

    while(true)
    {
        memset(&buffer[0],0,sizeof(buffer));
        bytes_received = recvfrom(sockfd, buffer, 1024, 0, (struct sockaddr*) &client_address, &addrlen);
        if(bytes_received!=-1)
        {
            // cout << "Hello from : \n" ;

            strcpy(without_garbage,buffer);
            unsigned long n = 175;

            for(int i = 0; i <4 ; i++)
            {
                without_garbage[i]=buffer[i];
            }
            without_garbage[4]='\0';
           
           
                //sending table------------------------------
            if(!strcmp(without_garbage,"clk ")){
                counter++;
                //printf("%s\n",buffer);
                //send_rout_table(my_ip);
            }
            
             //showing router table--------------------------------------
        if(!strcmp(without_garbage,"show"))
        {
            router->print_routing_table(hostip);
            
        }
        
        //update cost-------------------------------------
        if(!strcmp(without_garbage,"cost"))
        {
            char ip1[20],ip2[20],newcost[10];
            inet_ntop(AF_INET, buffer + 4, ip1, sizeof(ip1));
            //printf("%s\n", ip1);
            inet_ntop(AF_INET, buffer + 8, ip2, sizeof(ip2));
            //printf("%s\n", ip2);
            
            inet_ntop(AF_INET, buffer + 12, newcost, sizeof(newcost));
            newcost[2]='\0';
            string ip_1(ip1);
            string ip_2(ip2);
            string new_cost(newcost);
           // cout<<new_cost<<endl;
            
            int a[bytes_received];
            for(int i=4;i<bytes_received;i++){
		 		a[i-4] = (int)((unsigned char)buffer[i]) ;
		 		cout<<a[i-4]<<endl;
		    }
		   // cout<<a<<endl;
          
        }
        
                
        }
    }


    return 0;

}

