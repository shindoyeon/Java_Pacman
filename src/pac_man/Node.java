package pac_man;

public class Node
{
	int		f;			// f = g+h
	int		h;			// �޸���ƽ ��
	int		g;			// ��������� �Ÿ�
	int		x, y;		// ����� ��ġ
	Node	prev;		// ���� ���
	Node	direct[];	// ������ ���
	Node	next;		// ���� ���	
	//*************************************************************************
	// Name : Node()
	// Desc : ������
	//*************************************************************************
	Node()
	{
		direct = new Node[8];    //������ 8�� ��� 
		for( int i = 0; i < 8; i++) direct[i] = null;
	}
}
