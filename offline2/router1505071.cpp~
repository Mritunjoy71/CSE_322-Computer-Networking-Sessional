#include<bits/stdc++.h>
#include <arpa/inet.h>
#include <sys/socket.h>
#include <unistd.h>
using namespace std;
int hostip=0;
///total ip finding

int ipcount()
{
    FILE *fp;
    int count = 0;  // Line counter (result)
    char c;  // To store a character read nhop file
    fp = fopen("setup.txt", "r");
    if (fp == NULL)
    {
        printf("Could not open file ");
        return 0;
    }
    for (c = getc(fp); c != EOF; c = getc(fp))
        if (c == '\n') // Increment count if this character is newline
            count = count + 1;
    fclose(fp);
    //printf("The file %s has %d lines\n ", filename, count);

    return count;
}

///total ip
int nodes=ipcount()-1;

vector<pair<int, int> > table[20] ;

struct Node
{
    unsigned dist[10];
    unsigned nhop[10];

};

Node cpdvr[10];
class Router
{

public :
    Node DVR[10] ;
    int costmat[10][10];
    int nodes;

    Router(int nodes)
    {
        this->nodes=nodes;
        for(int i = 1; i <= nodes; i++)
        {
            for(int j = 1; j <= nodes; j++)
            {
                costmat[i][i] = INT_MAX;
                DVR[i].dist[j] = INT_MAX; //initialise the distance equal to -1
                costmat[i][j]=INT_MAX;
                DVR[i].nhop[j] =-1;

                DVR[j].dist[i] =INT_MAX; //initialise the distance equal to -1
                costmat[j][i]=INT_MAX;
                DVR[j].nhop[i] =-1;
            }
        }
    }
    ///routing table vector

    void addpath(int u,int v, int wt)
    {
        table[u].push_back(make_pair(v, wt));
        table[v].push_back(make_pair(u, wt));
    }

    void routing_table()
    {
        for(int i=1; i<=nodes; i++)
        {
            if(i != hostip)
            {
                addpath(i,DVR[hostip].nhop[i], DVR[hostip].dist[i]);
                //cout<<i<<"\t"<<DVR[hostip].nhop[i]<<"\t"<<DVR[hostip].dist[i]<<endl;
            }
        }

    }

    void dvr_protocol()
    {
        for(int k = 1; k <= nodes; k++)  //We choose arbitary vertex k and we calculate the
        {
            //direct distance nhop the Node i to k using the cost matrix and add the distance nhop k to Node j
            for(int i =1; i <= nodes; i++)
            {
                for(int j = 1; j <= nodes; j++)
                    if(DVR[i].dist[j] > DVR[i].dist[k] + DVR[k].dist[j])
                    {
                        //We calculate the minimum distance
                        DVR[i].dist[j] = DVR[i].dist[k] + DVR[k].dist[j];
                        DVR[j].dist[i] = DVR[i].dist[j];
                        DVR[i].nhop[j] = k;
                        DVR[j].nhop[i] = k;
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
                    if(DVR[i].nhop[j]==-1)
                        cout<<"--\t\t";
                    else
                        cout<<"192.168.10."<<DVR[i].nhop[j]<<"\t\t";
                    if(DVR[i].dist[j]==INT_MAX)
                        cout<<"\tinf";
                    else
                        cout<<DVR[i].dist[j];
                }


            }

            //cout<<"\n"<<j<<"\t\t"<<DVR[i].nhop[j]<<"\t\t"<<DVR[i].dist[j];
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
                if(DVR[ip].nhop[j]==-1)
                    cout<<"--\t\t";
                else
                {
                    cout<<"192.168.10."<<DVR[ip].nhop[j]<<"\t\t";
                }
                if(DVR[ip].dist[j]==INT_MAX)
                    cout<<"\tinf";
                else
                {
                    cout<<DVR[ip].dist[j];
                }
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
                if(DVR[ip].nhop[j]==-1)
                    cout<<"--\t\t";
                else
                    cout<<"192.168.10."<<DVR[ip].nhop[j]<<"\t\t";
                if(DVR[ip].dist[j]==INT_MAX)
                    cout<<"\tinf";
                else
                    cout<<DVR[ip].dist[j];
            }
            cout.flush();


        }
        cout<<"\n\n";
    }



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
                DVR[ip1].dist[ip2] =cost ;
                DVR[ip2].dist[ip1] =cost ;

                costmat[ip1][ip2]=cost;
                costmat[ip2][ip1]=cost;

                DVR[ip1].nhop[ip2] = ip2;
                DVR[ip2].nhop[ip1] = ip1;
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
        print_initial(hipnum);
        cout<<"\n\n";


        return hipnum;

    }



    void update_cost(int u,int v,int w)
    {
        copy(begin(cpdvr),end(cpdvr),begin(DVR));

        DVR[u].dist[v] =w ;
        DVR[v].dist[u] =w ;

        costmat[u][v]=w;
        costmat[v][u]=w;

        DVR[u].nhop[v] = v;
        DVR[v].nhop[u] = u;

        dvr_protocol();
        cout<<"\nAfter update cost\n";
        print_routing_table(hostip);

    }


};

///end of router class



Router *router=new Router(nodes);


void send_table()
{

    char buffer[1024];
    string check="tabl";

    for(int i=1; i<=nodes; i++)
    {
        if(i != hostip)
        {
            stringstream ss1,ss2,ss3;
            ss1<<i;
            ss2 << router->DVR[hostip].nhop[i];
            ss3<< router->DVR[hostip].dist[i];

            check=check+" "+ss1.str()+" "+ss2.str()+" "+ss3.str();
        }
    }

    strncpy(buffer, check.c_str(), sizeof(buffer));
    buffer[sizeof(buffer) - 1] = 0;

    for(int i=1; i<=nodes; i++)
    {
        if(i!=hostip && router->costmat[hostip][i]!=INT_MAX)
        {
            int sockfd;
            sockfd = socket(AF_INET, SOCK_DGRAM, 0);


            stringstream ss1;
            ss1<<i;
            string nh="192.168.10."+ss1.str();

            struct sockaddr_in server_address;
            server_address.sin_family = AF_INET;
            server_address.sin_port = htons(4747);
            server_address.sin_addr.s_addr = inet_addr(nh.c_str());

            int sent_bytes= sendto(sockfd, buffer, 1024, 0, (struct sockaddr*) &server_address, sizeof(sockaddr_in));
            if (sent_bytes != -1)
            {
                cout<<"table sent to "<<nh<<endl;
            }


        }

    }


}



int main(int argc, char *argv[])
{
    if(argc != 3)
    {
        printf("%s <ip address>\n", argv[0]);
        exit(1);
    }


    hostip=router->init_router(argv[1],argv[2]);

    copy(begin(router->DVR),end(router->DVR),begin(cpdvr));

    //cout<<"bal"<<cpdvr[2].dist[3]<<endl;

    router->dvr_protocol();
    // router->routing_table();

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
        bytes_received = recvfrom(sockfd, buffer, 1024, 0, (struct sockaddr*) &router_address, &addrlen);
        if(bytes_received!=-1)
        {
            // cout << "Hello nhop : \n" ;

            strcpy(without_garbage,buffer);
            unsigned long n = 175;

            for(int i = 0; i <4 ; i++)
            {
                without_garbage[i]=buffer[i];
            }
            without_garbage[4]='\0';

            ///sending table------------------------------
            if(!strcmp(without_garbage,"clk "))
            {
                counter++;
                send_table();
            }

            ///showing router table--------------------------------------
            if(!strcmp(without_garbage,"show"))
            {
                router->print_routing_table(hostip);

            }

            ///update cost-------------------------------------
            if(!strcmp(without_garbage,"cost"))
            {
                int a[bytes_received-1];
                for(int i=4; i<bytes_received; i++)
                {
                    a[i-4] = (int)((unsigned char)buffer[i]) ;
                    //cout<<a[i-4]<<endl;
                }

                int u,v,w;
                u=a[3];
                v=a[7];
                a[9]=a[9]<<8;
                w=a[8]+a[9];
                //cout<<ip1<<endl<<ip2<<endl<<newcost<<endl<<endl;
                router->update_cost(u,v,v);

                for(int i=1; i<=nodes; i++)
                {
                    if(hostip == u && (i!=u && i!=v))
                    {
                        stringstream ss1,ss2,ss3,ss4;
                        ss1<<i;
                        ss2<<u;
                        ss3<<v;
                        ss4<<w;
                        string nh="192.168.10."+ss1.str();

                        struct sockaddr_in server_address;
                        server_address.sin_family = AF_INET;
                        server_address.sin_port = htons(4747);
                        server_address.sin_addr.s_addr = inet_addr(nh.c_str());

                        string fm="updt "+ss2.str()+" "+ss3.str()+" "+ss4.str();
                        char buffer2[1024];
                        strncpy(buffer2, fm.c_str(), sizeof(buffer2));
                        buffer2[sizeof(buffer2) - 1] = 0;
                        int sent_bytes= sendto(sockfd, buffer2, 1024, 0, (struct sockaddr*) &server_address, sizeof(sockaddr_in));
                        if (sent_bytes != -1)
                        {
                            cout<<"\nupdate request sent to "<<nh<<endl;
                        }




                    }


                }

            }


            if(!strcmp(without_garbage,"updt"))
            {
                stringstream ss;
                for(int i=0; i<bytes_received; i++)
                {
                    ss << (char)((unsigned char)buffer[i]) ;

                }

                string fm=ss.str();
                // cout<<fm<<endl;

                stringstream ss1(fm);
                string temp;
                int count=0;
                string msg;
                int u,v,w;

                while(getline(ss1,temp, ' '))
                {
                    count++;
                    stringstream ss2(temp);
                    if(count==2)
                    {
                        ss2>>u;
                    }
                    else if(count==3)
                    {
                        ss2>>v;
                    }
                    else if(count==4)
                    {
                        ss2>>w;
                    }
                }
                router->update_cost(u,v,w);
            }



            if(!strcmp(without_garbage,"tabl"))
            {
                ;
            }

            if(!strcmp(without_garbage,"send"))
            {
                // cout<<"hello\n";

                int a[bytes_received-1];
                for(int i=4; i<bytes_received; i++)
                {
                    a[i-4] = (int)((unsigned char)buffer[i]) ;
                    //cout<<a[i-4]<<endl;
                }

                int ip1,ip2,msgl;
                ip1=a[3];
                ip2=a[7];
                a[9]=a[9]<<8;
                msgl=a[8]+a[9];
                //cout<<a[3]<<endl<<a[7]<<endl<<msgl<<endl;
                // cout<<bytes_received<<endl;

                char m[msgl];

                stringstream ss,ss1,ss2,ss3;
                for(int i=14; i<bytes_received; i++)
                {
                    ss2<<(char)((unsigned char)buffer[i]) ;
                }

                string message=ss2.str();

                int nexthop=router->DVR[ip1].nhop[ip2];
                ss3<<nexthop;
                string nh="192.168.10."+ss3.str();

                struct sockaddr_in server_address;
                server_address.sin_family = AF_INET;
                server_address.sin_port = htons(4747);
                server_address.sin_addr.s_addr = inet_addr(nh.c_str());

                string fwrd;

                ss << ip2;
                string str = ss.str();

                fwrd="frwd 192.168.10."+str+" ";

                ss1 << msgl;
                str = ss1.str();

                fwrd=fwrd+str+" "+ss2.str();

                // cout<<fwrd<<endl;

                char buffer1[1024];
                strncpy(buffer1, fwrd.c_str(), sizeof(buffer1));
                buffer1[sizeof(buffer1) - 1] = 0;
                int sent_bytes= sendto(sockfd, buffer1, 1024, 0, (struct sockaddr*) &server_address, sizeof(sockaddr_in));
                if (sent_bytes != -1)
                {
                    cout<<"\n"<<message <<" packet forwarded to "<<nh<<endl;
                }

            }


            if(!strcmp(without_garbage,"frwd"))
            {
                //cout<<"hi"<<endl;
                stringstream ss;
                for(int i=0; i<bytes_received; i++)
                {
                    ss << (char)((unsigned char)buffer[i]) ;

                }

                string fm=ss.str();
                // cout<<fm<<endl;

                stringstream ss1(fm);
                string temp;
                int count=0;
                string msg;

                while(getline(ss1,temp, ' '))
                {
                    count++;
                    if(count>=4)
                    {
                        msg+=" " +temp;
                    }
                }
                //cout<<msg<<endl;

                int destip = (int)((unsigned char)buffer[16]) -48;  //fwrd 192.168.10.4 5 hello
                //cout<<destip<<endl;

                if(destip==hostip)
                {
                    cout<<"\n"<<msg <<" packet reached destination"<<endl;

                }
                else
                {

                    int nexthop=router->DVR[hostip].nhop[destip];
                    stringstream ss2;
                    ss2<<nexthop;
                    string nh="192.168.10."+ss2.str();

                    struct sockaddr_in server_address;
                    server_address.sin_family = AF_INET;
                    server_address.sin_port = htons(4747);
                    server_address.sin_addr.s_addr = inet_addr(nh.c_str());

                    char buffer2[1024];
                    strncpy(buffer2, fm.c_str(), sizeof(buffer2));
                    buffer2[sizeof(buffer2) - 1] = 0;
                    int sent_bytes= sendto(sockfd, buffer2, 1024, 0, (struct sockaddr*) &server_address, sizeof(sockaddr_in));
                    if (sent_bytes != -1)
                    {
                        cout<<"\n"<<msg <<" packet forwarded to "<<nh<<endl;
                    }


                }

            }

        }
    }


    return 0;

}

