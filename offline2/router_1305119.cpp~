#include<iostream>
#include<stdlib.h>
#include<fstream>
#include<string>
#include<sstream>
#include<vector>
#include <cstdio>
#include<stdio.h>
#include <arpa/inet.h>
#include <sys/socket.h>
#include <string.h>
#include<string>
#include <unistd.h>
#include<algorithm>
#define max_value "10000"
using namespace std;

struct table_entry
{
    string dest;
    string next_hop;
    string cost;
};

struct neighbour_entry
{
    string neigh;
    string costs;
    int counting;
    int down;
};


vector<neighbour_entry> neighbour;


vector<table_entry> routing_table;

struct table_entry_local
{
    string dest_local;
    string next_hop_local;
    string cost_local;
};

vector<table_entry_local> routing_table_local;

void init_router(string my_ip,string topo_file)
{
    string line;
    ifstream myfile;
    myfile.open(topo_file.c_str());
    if (myfile)
    {
        int j=-1;
        while ( getline (myfile,line) )
        {
            istringstream iss(line);
            int i=0;
            j++;
            routing_table_local.push_back(table_entry_local());

            do
            {
                string sub;
                iss >> sub;
                if(i==0)
                    routing_table_local[j].dest_local=sub;

                else if(i==1)
                    routing_table_local[j].next_hop_local=sub;
                else if(i==2)
                    routing_table_local[j].cost_local=sub;
                else
                    i=0;
                i++;
                //cout<<sub<<"\n";
            }
            while (iss);

        }
        myfile.close();
    }

    else
        cout << "Unable to open file";

    /*for(int i = 0 ; i<routing_table_local.size() ; i++)
        cout<<routing_table_local[i].dest_local<<'\n' <<routing_table_local[i].next_hop_local<<'\n'<< routing_table_local[i].cost_local<<'\n' ;
    */



    int rout_ind=0;
    for(int i=0,g=0; i<routing_table_local.size(); i++)
    {
        if(routing_table_local[i].dest_local.compare(my_ip))
        {
            if(!routing_table_local[i].next_hop_local.compare(my_ip))
            {
                routing_table.push_back(table_entry());
                routing_table[rout_ind].dest=routing_table_local[i].dest_local;
                routing_table[rout_ind].next_hop=routing_table_local[i].dest_local;
                routing_table[rout_ind].cost=routing_table_local[i].cost_local;
                rout_ind++;
                //neighbour.push_back(routing_table_local[i].dest_local);
                //cout<<neighbour[g++]<<endl;
                neighbour.push_back(neighbour_entry());
                neighbour[g].neigh=routing_table_local[i].dest_local;
                //cout<<neighbour[g].neigh;
                neighbour[g].costs=routing_table_local[i].cost_local;
                neighbour[g].counting=0;
                neighbour[g].down=0;
                g++;
            }

            else
            {
                int flag = 1;
                for(int j = 0 ; j<routing_table.size() ; j++)
                {
                    if(!routing_table[j].dest.compare(routing_table_local[i].dest_local))
                    {
                        flag = 0;
                        break;
                    }
                }
                if(flag)
                {
                    routing_table.push_back(table_entry());
                    routing_table[rout_ind].dest=routing_table_local[i].dest_local;
                    routing_table[rout_ind].next_hop="";
                    routing_table[rout_ind].cost=max_value;
                    rout_ind++;
                }

                flag = 1;
                for(int j = 0 ; j<routing_table.size() ; j++)
                {
                    if(!routing_table[j].dest.compare(routing_table_local[i].next_hop_local))
                    {
                        flag = 0;
                        break;
                    }
                }
                if(flag)
                {
                    routing_table.push_back(table_entry());
                    routing_table[rout_ind].dest=routing_table_local[i].next_hop_local;
                    routing_table[rout_ind].next_hop="";
                    routing_table[rout_ind].cost=max_value;
                    rout_ind++;
                }






            }
        }
        else
        {
            /*for(int j = 0;j<dest.size();j++){
                if(!dest[j].compare(ip2[i])){
                    dest.erase(dest.begin()+j);
                    next_hop.erase(next_hop.begin()+j);
                    cost.erase(cost.begin()+j);
                    break;
                }
            }
            */
            routing_table.push_back(table_entry());
            routing_table[rout_ind].dest=routing_table_local[i].next_hop_local;
            routing_table[rout_ind].next_hop=routing_table_local[i].next_hop_local;
            routing_table[rout_ind].cost=routing_table_local[i].cost_local;
            rout_ind++;
            neighbour.push_back(neighbour_entry());
            neighbour[g].neigh=routing_table_local[i].next_hop_local;
            //cout<<neighbour[g].neigh;
            neighbour[g].costs=routing_table_local[i].cost_local;
            neighbour[g].counting=0;
            neighbour[g].down=0;
            g++;
        }

    }


    for(int i = 0 ; i<routing_table.size() ; i++)
    {
        string str_i = routing_table[i].dest;
        for(int j = 0 ; j<routing_table.size() ; j++)
        {
            if(i==j)
                continue;
            string str_j = routing_table[j].dest;
            if(!str_i.compare(str_j))
            {
                if(!routing_table[j].next_hop.compare(""))
                    routing_table.erase(routing_table.begin()+j);
                else
                {
                    routing_table.erase(routing_table.begin()+i);
                    i--;
                }
            }

        }

    }

    cout<<"destination                next hop                        cost\n";
    cout<<"-----------                --------                        ----\n";

    for(int i = 0 ; i<routing_table.size() ; i++)
    {
        cout<<routing_table[i].dest<<"              "<<routing_table[i].next_hop<<"                            "<<routing_table[i].cost<<'\n' ;
    }


    return;
}


bool is_not_ascii(char c)
{
    return !(c >= 0 && c <= 127);
}


string get_cost(string str_dest)
{
    for(int i = 0 ; i<routing_table.size() ; i++)
        if(!routing_table[i].dest.compare(str_dest))
            return routing_table[i].cost;

}



void send_rout_table(string ip)
{
    int sockfd;
    sockfd = socket(AF_INET, SOCK_DGRAM, 0);
    char rout_string[1024];
    string check="tabl+";
    check+=ip+"+";
    for(int i = 0 ; i< routing_table.size() ; i++)
    {
        check.append(routing_table[i].dest);
        check.append("*");
        check.append(routing_table[i].next_hop);
        check.append("*");
        check.append(routing_table[i].cost);
        check.append("*");
    }
    strcpy(rout_string,check.c_str());
    //printf("%s",rout_string);
    for(int i = 0 ; i<neighbour.size() ; i++)
    {
        //if(!routing_table[i].dest.compare(routing_table[i].next_hop)){
        //printf("%s\n",frwd_massage);
        //sending packect to next_hop router
        struct sockaddr_in neighbour_Address;
        //memset(frwd_massage,'\0',1024);
        neighbour_Address.sin_family = AF_INET;
        neighbour_Address.sin_port = htons(4747);
        inet_pton(AF_INET,neighbour[i].neigh.c_str(),&neighbour_Address.sin_addr);

        int sent_bytes = sendto(sockfd,rout_string, 1024, 0, (struct sockaddr*) &neighbour_Address, sizeof(sockaddr_in));
        //printf("%d\n",sent_bytes);
        //if (sent_bytes != -1)
        //printf("routing table sent to %s\n",routing_table[i].next_hop.c_str());

        //sending complete
        //break;
        //}
    }

}


int main(int argc, char *argv[])
{
    string my_ip,topo_file;
    char without_garbage[100];
    my_ip=argv[1];
    topo_file=argv[2];

    init_router(my_ip,topo_file); //showing initial routing table
    //send_rout_table();
    int counter = 0;
    int sockfd;
    int bind_flag;
    int bytes_received;
    socklen_t addrlen;
    char buffer[1024];
    struct sockaddr_in server_address;
    struct sockaddr_in client_address;

    server_address.sin_family = AF_INET;
    server_address.sin_port = htons(4747);
    inet_pton(AF_INET,my_ip.c_str(), &server_address.sin_addr);

    sockfd = socket(AF_INET, SOCK_DGRAM, 0);

    bind_flag = bind(sockfd, (struct sockaddr*) &server_address, sizeof(sockaddr_in));

    if(bind_flag==0)
        printf("successful bind.\n");


    while(true)
    {
        memset(&buffer[0],0,sizeof(buffer));
        bytes_received = recvfrom(sockfd, buffer, 1024, 0, (struct sockaddr*) &client_address, &addrlen);

        //printf("[%s:%hu]: %s\n\n\n", inet_ntoa(client_address.sin_addr), ntohs(client_address.sin_port), buffer);
        //printf("%s\n",buffer);

        strcpy(without_garbage,buffer);
        unsigned long n = 175;


        for(int i = 0; i <4 ; i++)
        {
            without_garbage[i]=buffer[i];
        }
        without_garbage[4]='\0';





        //sending table------------------------------
        if(!strcmp(without_garbage,"clk "))
        {
            counter++;
           // printf("%s\n",buffer);
            send_rout_table(my_ip);
        }

        //updating------------------

        if(!strcmp(without_garbage,"tabl"))
        {
            string str_use(buffer);
            int flag=0;
            char ip_add[20];
            //cout<<str_use;
            char str1[20],str2[20],str3[20];
           // cout<<"\nkmn acho?\n";
            for(int i = 0,j=0,k=0,l=0,p=0; i <str_use.size() ; i++)
            {
                if(l==0)
                {
                    if(str_use[i]=='+')
                    {
                        if(flag==1)
                        {
                            l=1;
                            j=1;
                            ip_add[p]='\0';
                            //changing
                            //for down , check counting----------
                            string recent(ip_add);
                            for(int w = 0 ; w<neighbour.size() ; w++)
                                if(!neighbour[w].neigh.compare(recent))
                                    neighbour[w].counting++;


                            //changing
                            continue;
                        }
                        flag++;
                        continue;
                    }
                    if(flag==1)
                    {
                        ip_add[p++]=str_use[i];
                    }

                    continue;
                }



                if(str_use[i]=='*')
                {
                    j++;
                    if(j==2)
                        str1[k]='\0';
                    else if(j==3)
                        str2[k]='\0';
                    else if(j==4)
                    {
                        j=1;
                        str3[k]='\0';

                        //Update routing table---------------------------------------
                        string destination(str1), nexthop(str2), costing(str3);
                        string my_ipadd(ip_add);

                        //cout<<destination<<"  "<<nexthop<<"  "<<costing<<"  "<<my_ipadd<<endl;

                        for(int v=0 ; v<routing_table.size() ; v++)
                            if(!routing_table[v].dest.compare(destination) && nexthop.compare(""))
                            {
                                int num=atoi(costing.c_str())+atoi(get_cost(my_ipadd).c_str());
                                if((num < atoi(routing_table[v].cost.c_str()) && nexthop.compare(my_ip)) || (!routing_table[v].next_hop.compare(my_ipadd)))   //using split horizon----------- and force update--------
                                {
                                    routing_table[v].next_hop=my_ipadd;
                                    stringstream ss;
                                    ss << num;
                                    //string str = ss.str();
                                    routing_table[v].cost=ss.str();
                                    //cout<<num<<endl;
                                    //cout<<costing<<"  "<<get_cost(my_ipadd)<<"  "<<routing_table[v].cost<<endl;
                                    //cout<<num<<"  "<<atoi(costing.c_str())<<"  "<<atoi(get_cost(my_ipadd).c_str())<<"  "<<atoi(routing_table[v].cost.c_str())<<"----------\n";


                                }


                            }



                    }
                    k=0;
                    continue;
                }
                if(j==1)
                {
                    str1[k++]=str_use[i];
                }
                else if(j==2)
                {
                    str2[k++]=str_use[i];
                }
                else if(j==3)
                {
                    str3[k++]=str_use[i];
                }
            }

        }






        //showing router table--------------------------------------
        if(!strcmp(without_garbage,"show"))
        {
            cout<<"destination                next hop                        cost\n";
            cout<<"-----------                --------                        ----\n";

            for(int i = 0 ; i<routing_table.size() ; i++)
            {
                cout<<routing_table[i].dest<<"              "<<routing_table[i].next_hop<<"                            "<<routing_table[i].cost<<'\n' ;
            }
        }




        //update cost-------------------------------------
        if(!strcmp(without_garbage,"cost"))
        {
            char ip1[20],ip2[20],locost[10];
            inet_ntop(AF_INET, buffer + 4, ip1, sizeof(ip1));// printf("%s\n", ip1);
            inet_ntop(AF_INET, buffer + 8, ip2, sizeof(ip2));// printf("%s\n", ip2);
            //for(int i = 12,k=0; i <14 ; i++,k++){locost[k]=buffer[i];}
            //locost[2]='\0';
            inet_ntop(AF_INET, buffer + 12, locost, sizeof(locost));
            locost[2]='\0';
            string ip_1(ip1);
            string ip_2(ip2);
            string lo_cost(locost);
            //cout<<lo_cost;
            if(!ip_1.compare(my_ip))
                for(int i = 0 ; i<routing_table.size() ; i++)
                {
                    if(!routing_table[i].dest.compare(ip_2))
                    {
                        routing_table[i].cost=lo_cost;
                        for(int t = 0 ; t<neighbour.size() ; t++)
                            if(!neighbour[t].neigh.compare(ip_2))
                                neighbour[t].costs = lo_cost;

                    }

                }

            else
                for(int i = 0 ; i<routing_table.size() ; i++)
                {
                    if(!routing_table[i].dest.compare(ip_1))
                    {
                        routing_table[i].cost=lo_cost;
                        for(int t = 0 ; t<neighbour.size() ; t++)
                            if(!neighbour[t].neigh.compare(ip_1))
                                neighbour[t].costs = lo_cost;
                    }

                }
        }




        //received send massage from router--------------------------------
        if(!strcmp(without_garbage,"send"))
        {
            char ip1[20],ip2[20],massage[40],frwd_massage[1024];
            //inet_ntop(AF_INET, buffer + 4, ip1, sizeof(ip1));// printf("%s\n", ip1);
            inet_ntop(AF_INET, buffer + 8, ip2, sizeof(ip2));// printf("%s\n", ip2);
            //string ip_1(ip1);
            string ip_2(ip2);
            int temp = (unsigned char)buffer[13];
            int temp1 = (unsigned char)buffer[12];
            temp1 = temp << 8 | temp1;
            printf("%d\n",temp1);

            for(int i = 0 ; i<temp1 ; i++)
            {
                massage[i]=buffer[14+i];
            }
            massage[temp1]='\0';
            snprintf(ip1, sizeof(ip1), "%d", temp1);
            //printf("%s\n",ip1);
            strcpy(frwd_massage,"frwd");
            strcat(frwd_massage,"*");
            strcat(frwd_massage,ip2);
            strcat(frwd_massage,"*");
            strcat(frwd_massage,ip1);
            strcat(frwd_massage,"*");
            strcat(frwd_massage,massage);

            if(!ip_2.compare(my_ip))
                printf("%s packet reached destination.\n",massage);
            else
                for(int i = 0 ; i<routing_table.size() ; i++)
                {
                    if(!routing_table[i].dest.compare(ip_2) && routing_table[i].cost.compare(max_value))
                    {
                        //printf("%s\n",frwd_massage);
                        //sending packect to next_hop router
                        struct sockaddr_in neighbour_Address;
                        //memset(frwd_massage,'\0',1024);
                        neighbour_Address.sin_family = AF_INET;
                        neighbour_Address.sin_port = htons(4747);
                        inet_pton(AF_INET,routing_table[i].next_hop.c_str(),&neighbour_Address.sin_addr);

                        int sent_bytes = sendto(sockfd,frwd_massage, 1024, 0, (struct sockaddr*) &neighbour_Address, sizeof(sockaddr_in));
                        if (sent_bytes != -1)
                        {
                            printf("%s packet forwarded to %s\n", massage,routing_table[i].next_hop.c_str());
                        }
                        //sending complete
                        break;
                    }
                }
        }




        //receiving massage from other router
        if(!strcmp(without_garbage,"frwd"))
        {
            string str_use(buffer);
            //cout<<str_use;
            char str1[20],str2[10],str3[40];
            for(int i = 0,j=0,k=0; i <str_use.size() ; i++)
            {
                if(str_use[i]=='*')
                {
                    j++;
                    if(j==2)
                        str1[k]='\0';
                    else if(j==3)
                        str2[k]='\0';
                    k=0;
                    continue;
                }
                if(j==1)
                {
                    str1[k++]=str_use[i];
                }
                else if(j==2)
                {
                    str2[k++]=str_use[i];
                }
                else if(j==3)
                {
                    str3[k++]=str_use[i];
                }
                if(i==str_use.size()-1)
                    str3[k]='\0';
            }
            //printf("%s %s %s\n",str1,str2,str3);


            char frwd_massage[1024];
            //char ip1[20],ip2[20],massage[40],frwd_massage[1024];
            //inet_ntop(AF_INET, buffer + 4, ip1, sizeof(ip1));// printf("%s\n", ip1);
            //inet_ntop(AF_INET, buffer + 4, ip2, sizeof(ip2));// printf("%s\n", ip2);
            //string ip_1(ip1);
            //strcpy(ip2,str1);
            string ip_2(str1);
            //int temp = (unsigned char)buffer[9];
            //int temp1 = atoi(str2);
            //temp1 = temp << 8 | temp1;
            //printf("%d , %s\n",temp1,ip2);

            //for(int i = 0 ; i<temp1 ; i++)
            //{
            //massage[i]=buffer[10+i];
            //}
            //massage[temp1]='\0';
            //snprintf(ip1, sizeof(ip1), "%d", temp1);
            //printf("%s\n",ip1);
            strcpy(frwd_massage,"frwd");
            strcat(frwd_massage,"*");
            strcat(frwd_massage,str1);
            strcat(frwd_massage,"*");
            strcat(frwd_massage,str2);
            strcat(frwd_massage,"*");
            strcat(frwd_massage,str3);

            if(!ip_2.compare(my_ip))
                printf("%s packet reached destination.\n",str3);
            else
                for(int i = 0 ; i<routing_table.size() ; i++)
                {
                    if(!routing_table[i].dest.compare(ip_2) && routing_table[i].cost.compare(max_value))
                    {
                        //printf("%s\n",frwd_massage);
                        //sending packect to next_hop router
                        struct sockaddr_in neighbour_Address;
                        //memset(frwd_massage,'\0',1024);
                        neighbour_Address.sin_family = AF_INET;
                        neighbour_Address.sin_port = htons(4747);
                        inet_pton(AF_INET,routing_table[i].next_hop.c_str(),&neighbour_Address.sin_addr);

                        int sent_bytes = sendto(sockfd,frwd_massage, 1024, 0, (struct sockaddr*) &neighbour_Address, sizeof(sockaddr_in));
                        if (sent_bytes != -1)
                        {
                            printf("%s packet forwarded to %s\n", str3,routing_table[i].next_hop.c_str());
                        }
                        //sending complete
                        break;
                    }
                }
        }

        //code for down and up ; cost updated
        if(counter==3)
        {
            for(int w = 0 ; w<neighbour.size() ; w++)
            {
                if(neighbour[w].counting==neighbour.size() && neighbour[w].down == 1)
                {
                    neighbour[w].down = 0;
                    //change cost
                    for(int t = 0 ; t<routing_table.size() ; t++)
                        if(!routing_table[t].next_hop.compare(neighbour[w].neigh))
                            routing_table[t].cost = neighbour[w].costs;
                }
                if(neighbour[w].counting==0)
                {
                    neighbour[w].down = 1;
                    //change cost
                    for(int t = 0 ; t<routing_table.size() ; t++)
                        if(!routing_table[t].next_hop.compare(neighbour[w].neigh))
                            routing_table[t].cost = max_value;
                }

                cout<<neighbour[w].counting;
                neighbour[w].counting=0;

            }

            counter = 0;
        }
    }
    return 0;
}

