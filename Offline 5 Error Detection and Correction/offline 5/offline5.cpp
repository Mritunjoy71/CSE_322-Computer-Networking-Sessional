#include <bits/stdc++.h>
#include "colormod.h"
using namespace std;
bool isPowerOfTwo(int n)
{
    return (ceil(log2(n)) == floor(log2(n)));
}

int main()
{
    Color::Modifier red(Color::FG_RED);
    Color::Modifier def(Color::FG_DEFAULT);
    Color::Modifier green(Color::FG_GREEN);
    Color::Modifier cyan(Color::FG_CYAN);

	mt19937_64 rng;
    // initialize the random number generator with time-dependent seed
    uint64_t timeSeed =chrono::high_resolution_clock::now().time_since_epoch().count();
    seed_seq ss{uint32_t(timeSeed & 0xffffffff), uint32_t(timeSeed>>32)};
    rng.seed(ss);
    // initialize a uniform distribution between 0 and 1
    uniform_real_distribution<double> unif(0, 1);

    string gps,data,block="";
    //stringstream ss;
    //int arra[8]={12,21,29,38,46,54,62,71};
    int l,m,gp,pad,g_size,rs,init_l;
    float p;
    vector<int> g;
    printf("enter data string : ");
    getline(cin,data);
    //cout<<data;
    cout<<endl;
    l=data.size();
    init_l=l;
    //cout<<"data size: "<<l<<endl;
    cout<<"enter number of data bytes in a row <m> : ";
    cin>>m;
    cout<<"enter probability : ";
    cin>>p;
    cout<<"enter generator polynomial : ";
    cin>>gps;
    g_size=gps.size();
    rs=g_size-1;
    gp=stoi(gps, nullptr, 2);

    for (int i = 0; i <g_size; ++i)
    {
        if (gps[i]=='1')
        {
            g.push_back(1);
        }
        else
        {
            g.push_back(0);
        }
    }
    //cout<<"generator polynomial "<<gp<<endl;

    if (l%m==0)
    {
        pad=0;
    }
    else
    {
        pad=m-l%m;
    }

    //cout<<"padding "<<pad<<endl;
    if (pad>0)
    {
        for (int i = 0; i < pad; ++i)
        {
            data=data+"~";
        }
    }
    cout<<"data string after padding :"<<data<<endl;

    l=data.size();
    int row=l/m;
    vector<int> vec[row];
    vector<int> cb_vec[row];//with check bit

    int a=m*8;
    int r=0;

    while (a + r + 1 > pow (2, r))
    {
        r++;
    }

    for (int i = 0; i < l; ++i)
    {
        bitset<8> x(data[i]);
        block=block+x.to_string();

    }
    int count=0;

    cout<<"\ndata block < ascii code of m characters per row > :\n";
    for (int i = 0; i < row; ++i)
    {
        for (int j = 0; j < m*8; ++j)
        {
            //cout<<block[count];
            if(block[count]=='1')
            {
                vec[i].push_back(1);
            }
            else
            {
                vec[i].push_back(0);
            }
            cout<<vec[i][j];
            count++;
        }
        cout<<endl;
    }
//initialize check bit vector
    for (int i = 0; i < row; ++i)
    {
        for (int j = 0; j < a+r; ++j)
        {
            cb_vec[i].push_back(0);

        }
    }

    int count1=0;
    for (int i = 0; i < row; ++i)
    {
        for (int j = 0; j < a+r ; ++j)
        {
            if (isPowerOfTwo(j+1))
            {

            }
            else
            {
                cb_vec[i][j]=vec[i][count1];
                count1++;
            }
            //cout<<cb_vec[i][j];
        }
        count1=0;
        //cout<<endl;
    }


    for (int k = 0; k < row; ++k)
    {
        int d1 = 0;
        int min, max = 0, parity, s, j;
        //Parity Bit Calculation
        for (int i = 1; i <= a + r; i = pow (2, d1))
        {
            ++d1;
            parity = 0;
            j = i;
            s = i;
            min = 1;
            max = i;
            for (j; j <= a + r;)
            {
                for (s = j; max >= min && s <= a + r; ++min, ++s)
                {
                    if (cb_vec[k][s-1] == 1)
                        parity++;
                }
                j = s + i;
                min = 1;
            }
            if (parity % 2 == 0) // Even Parity
            {
                cb_vec[k][i-1] = 0;
            }
            else
            {
                cb_vec[k][i-1] = 1;
            }
        }
    }

    cout<<"\ndata block after adding check bits:\n";

    for (int i = 0; i < row; ++i)
    {
        for (int j = 0; j < a+r ; ++j)
        {
            if(isPowerOfTwo(j+1))
            {
                cout<<green<<cb_vec[i][j];
            }
            else
            {
                cout<<def<<cb_vec[i][j];
            }
        }
        cout<<endl;
    }

    vector<int> col_vec;
    for (int i = 0; i < a+r; ++i)
    {
        for (int j = 0; j < row; ++j)
        {
            col_vec.push_back(cb_vec[j][i]);
            //cout<<cb_vec[j][i];
        }
    }
    cout<<"\ndata bits after column-wise serialization:\n";
    for (int i = 0; i < col_vec.size(); ++i)
    {
        cout<<col_vec[i];
    }

    //crc checksum

    vector<int> temp;

    for (int i=0; i<col_vec.size(); i++)
    {
        temp.push_back(col_vec[i]);
    }
    for (int i = 0; i < rs; ++i)
    {
        temp.push_back(0);
    }


    //Division
    int fs=col_vec.size();
    int i,j,k;
    for(i=0; i<fs; i++)
    {
        j=0;
        k=i;
        //check whether it is divisible or not
        if (temp[k]>=g[j])
        {
            for(j=0,k=i; j<g_size; j++,k++)
            {
                if((temp[k]==1 && g[j]==1) || (temp[k]==0 && g[j]==0))
                {
                    temp[k]=0;
                }
                else
                {
                    temp[k]=1;
                }
            }
        }
    }

    //CRC
    int crc[15];
    for(i=0,j=fs; j<fs+rs && i<rs; i++,j++)
    {
        crc[i]=temp[j];
        col_vec.push_back(crc[i]);
    }

    /*cout<<"\nCRC bits: ";
    for(i=0; i<rs; i++)
    {
        cout<<crc[i];
    }
    cout<<endl;
    */


    cout<<"\n\ndata bits after appending CRC checksum < sent frame >:\n";

    for (int i = 0; i < col_vec.size(); ++i)
    {
        if(i>=fs)
        {
            cout<<cyan<<col_vec[i];
        }
        else
        {
            cout<<def<<col_vec[i];
        }
    }
    cout<<endl;

    cout<<def<<"\nreceived frame:\n";
    int emap[fs+rs];
    for (int i = 0; i < fs+rs; ++i)
    {
        emap[i]=0;
    }

    for (int i = 0; i < col_vec.size(); ++i)
    {
        double r = unif(rng);
        if(r<=p)
        {
            emap[i]=1;
            if (col_vec[i]==0)
            {
                col_vec[i]=1;
                cout<<red<<col_vec[i];
            }
            else
            {
                col_vec[i]=0;
                cout<<red<<col_vec[i];
            }
        }
        else
        {
            cout<<def<<col_vec[i];
        }
    }
    cout<<endl;

    vector<int> temp2;
    for (int i=0; i<col_vec.size(); i++)
    {
        temp2.push_back(col_vec[i]);
    }

    //Division
    for(i=0; i<fs; i++)
    {
        j=0;
        k=i;
        if (temp2[k]>=g[j])
        {
            for(j=0,k=i; j<g_size; j++,k++)
            {
                if((temp2[k]==1 && g[j]==1) || (temp2[k]==0 && g[j]==0))
                {
                    temp2[k]=0;
                }
                else
                {
                    temp2[k]=1;
                }
            }
        }
    }

    cout<<"\nReaminder: ";
    int rrem[15];
    for (i=fs,j=0; i<fs+rs && j<rs; i++,j++)
    {
        rrem[j]= temp2[i];
    }
    for(i=0; i<rs; i++)
    {
        cout<<rrem[i];
    }

    int r_flag=0;
    for(i=0; i<rs; i++)
    {
        if(rrem[i]!=0)
        {
            r_flag=1;
        }
    }
    if (r_flag)
    {
        cout<<def<<"\nresult of CRC checksum matching: error detected\n";
    }
    else
    {
        cout<<def<<"\nresult of CRC checksum matching: no error detected\n";
    }

    cout<<def<<"\ndata block after removing crc checksum bits\n";
    count=0;
    int color[row][a+r];
    memset(color,0,sizeof(color));
    for (int i = 0; i < a+r; ++i)
    {
        for (int j = 0; j < row; ++j)
        {
            cb_vec[j][i]=col_vec[count];
            if (emap[count]==1)
            {
            	color[j][i]=1;
            }
            count++;

        }
    }

    int total_err[row];
    for (int i = 0; i < row; ++i)
    {
    	int n_err=0;
        for (int j = 0; j < a+r ; ++j)
        {
            if (color[i][j])
            {
                cout<<red<<cb_vec[i][j];
                n_err++;
            }
            else
            {
                cout<<def<<cb_vec[i][j];
            }

        }
        total_err[i]=n_err;
        cout<<endl;
    }



    //1 bit error correction
    string err_pos="";

    for (int k = 0; k < row; ++k)
    {
    	if (total_err[k]!=1)
    	{
    		continue;
    	}
        int d1 = 0;
        int min, max = 0, parity, s, j;
        //Parity Bit Calculation
        for (int i = 1; i <= a + r; i = pow (2, d1))
        {
            ++d1;
            parity = 0;
            j = i;
            s = i;
            min = 1;
            max = i;
            for (j; j <= a + r;)
            {
                for (s = j; max >= min && s <= a + r; ++min, ++s)
                {
                    if (cb_vec[k][s-1] == 1)
                        parity++;
                }
                j = s + i;
                min = 1;
            }
            if (parity % 2 == 0)
            {
            	err_pos="0"+err_pos;
            }
            else{
            	err_pos="1"+err_pos;
            }
        }
        int pos=stoi(err_pos, nullptr, 2);
        err_pos="";
        cout<<"\nerror position row "<<k+1<<" position "<<pos<<endl;
        if (cb_vec[k][pos-1]==1)
        {
        	cb_vec[k][pos-1]=0;	
        }
        else{
        	cb_vec[k][pos-1]=1;
        }

    }


    cout<<"\ndata block after removing check bits:\n";

    vector<int > final[row];
    for (int i = 0; i < row; ++i)
    {
        for (int j = 0; j < a+r ; ++j)
        {
        	if (!isPowerOfTwo(j+1))
        	{
        		final[i].push_back(cb_vec[i][j]);
        		cout<<cb_vec[i][j];
        	}
        }
        cout<<endl;
    }

    cout<<"\nOutput frame:";
    string str="";
    count=1;
    for (int i = 0; i < row; ++i)
    {
    	for (int j = 0; j < a; ++j)
    	{
    		if (final[i][j]==1)
    		{
    			str=str+"1";
    		}
    		else{
    			str=str+"0";	
    		}
    		if ((j+1)%8==0 && count<=init_l) //&& count<=init_l
    		{
    			int mask=stoi(str, nullptr, 2);
    			char ch=mask;
    			cout<<ch;
    			str="";	
    			count++;
    		}
    	}
    }

    cout<<endl;


    return 0;
}
