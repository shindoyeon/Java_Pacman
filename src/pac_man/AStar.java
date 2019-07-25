package pac_man;
//******************************************************************************
// File Name	: AStar.java
// Description	: A* �˰����� ����� ��ã�� Ŭ����
// Create		: 2003/04/01 JongHa Woo
// Update		:
//******************************************************************************
public class AStar
{
	// ���� ���, ���� ��� ����Ʈ
	Node	OpenNode, ClosedNode;
	
	// �� �迭
	int		iMap[];
	
	// �ִ� ���� ȸ��
	static final int LIMIT_LOOP = 1000;
	
	//*************************************************************************
	// Name : AStar()
	// Desc : ������
	//*************************************************************************
	AStar(int iM[])
	{
		// �� �迭�� ����
		iMap = iM;
		
		OpenNode = null;
		ClosedNode = null;
	}

	//*************************************************************************
	// Name : ResetPath()
	// Desc : ������ ������ ��θ� ����
	//*************************************************************************
	void ResetPath()
	{
		Node tmp;
	
		while( OpenNode != null )
		{
			tmp = OpenNode.next;
			OpenNode = null;
			OpenNode = tmp;
		}

		while( ClosedNode != null )
		{
			tmp = ClosedNode.next;
			ClosedNode = null;
			ClosedNode = tmp;
		}
	}

	//*************************************************************************
	// Name : FindPath()
	// Desc : ������ġ�� ��ǥ��ġ�� �Է� �޾� ��γ�� ����Ʈ�� ��ȯ
	//*************************************************************************
	Node FindPath(int sx, int sy, int tx, int ty)
	{
		Node	src, best = null;
		int		count = 0;
		
		// ó�� ���۳�� ����
		src = new Node();
		src.g = 0;
		src.h = (tx-sx)*(tx-sx) + (ty-sy)*(ty-sy);  
		src.f = src.h;
		src.x = sx;
		src.y = sy;
		
		// ���۳�带 ������� ����Ʈ�� �߰�
		OpenNode = src;
		
		// ��ã�� ���� ����
		// �ִ� �ݺ� ȸ���� ������ ��ã�� ����
		while( count < LIMIT_LOOP )
		{
			// ������尡 ���ٸ� ��� ��带 �˻������Ƿ� ��ã�� ����
			if( OpenNode == null )
			{
				return best;
			}
			
			
			// ��������� ù��° ��带 �������� ������忡�� ����
			best = OpenNode;
			OpenNode = best.next;
			
			// ������ ��带 ������忡 �߰�
			best.next = ClosedNode;
			ClosedNode = best;
			
			
			// ���� ������ ��尡 ���ٸ� ��ã�� ����
			if( best == null )
			{
				return null;
			}
			
			// ���� ������ ��尡 ��ǥ����� ��ã�� ����
			if( best.x == tx && best.y == ty )
			{
				return best;
			}
			
			// ���� ���� ������ ����� Ȯ���Ͽ� �������� �߰�
			if( MakeChild(best, tx, ty) == 0 && count == 0 )
			{
				return null;
			}
		
			count++;
		}
	
		return best;
	}


	//*************************************************************************
	// Name : MakeChild()
	// Desc : �Է¹��� ����� ������ ����� Ȯ��
	//*************************************************************************
	char MakeChild(Node node, int tx, int ty)
	{
		int		x, y;
		char	flag = 0;
		char	cc[] = {0, 0, 0, 0, 0, 0, 0, 0};
		
		x = node.x;
		y = node.y;
		
		// ������ ���� �̵��������� �˻�
		cc[0] = IsMove(x  , y+1);   //��
		cc[2] = IsMove(x-1, y  );   //����
		cc[4] = IsMove(x  , y-1);   //��
		cc[6] = IsMove(x+1, y  );   //������
		
		// �̵������� �����̶�� ��带 �����ϰ� �򰡰� ���
		if( cc[2] == 1 )
		{
			MakeChildSub(node, x-1, y, tx, ty);
			flag = 1;
		}
		
		if( cc[6] == 1 )
		{
			MakeChildSub(node, x+1, y, tx, ty);
			flag = 1;
		}
		
		if( cc[4] == 1 )
		{
			MakeChildSub(node, x, y-1, tx, ty);
			flag = 1;
		}
		
		if( cc[0] == 1 )
		{
			MakeChildSub(node, x, y+1, tx, ty);
			flag = 1;
		}
		/*�밢�� ���� ���
		if( cc[7] == 1 && cc[6] == 1 && cc[0] == 1 )
		{
			MakeChildSub(node, x+1, y+1, tx, ty);
			flag = 1;
		}
		
		if( cc[3] == 1 && cc[2] == 1 && cc[4] == 1 )
		{
			MakeChildSub(node, x-1, y-1, tx, ty);
			flag = 1;
		}

		if( cc[5] == 1 && cc[4] == 1 && cc[6] == 1 )
		{
			MakeChildSub(node, x+1, y-1, tx, ty);
			flag = 1;
		}

		if( cc[1] == 1 && cc[0] == 1 && cc[2] == 1 )
		{
			MakeChildSub(node, x-1, y+1, tx, ty);
			flag = 1;
		}
		*/
	
		return flag;
	}
	//*************************************************************************
	// Name : IsMove()
	// Desc : �̵������� ��ġ���� �˻�
	//*************************************************************************
	char IsMove(int x, int y)
	{
		// �̵� �Ұ����� ��ġ���� �˻�
		if( x < 0 || x > 13 || y < 0 || y > 13 || (iMap[y*14 + x] == 1) )
		{
			return 0;
		}
		
		return 1;
	}


	//*************************************************************************
	// Name : MakeChildSub()
	// Desc : ��带 ����. ������峪 ������忡 �̹� �ִ� ����� 
	//        �������� ���Ͽ� f�� �� ������ ���� ����
	//        ������忡 �ִٸ� �׿� ����� ��� ������ ������ ���� ����
	//*************************************************************************
	void MakeChildSub(Node node, int x, int y, int tx, int ty)
	{
		Node	old = null, child = null;
		int		i;
		int		g = node.g + 1;
				
		// �����尡 ���� ��忡 �ְ� f�� �� ������ ���� ����
		if( (old = IsOpen(x, y)) != null )
		{
			for( i = 0; i < 8; i++ )
			{
				if( node.direct[i] == null )
				{
					node.direct[i] = old;
					break;
				}
			}
			
			if( g < old.g )
			{
				old.prev = node;
				old.g = g;
				old.f = old.h + old.g;
			}
		}
		
		
		
		// �����尡 ���� ��忡 �ְ� f�� �� ������ ���� ����
		else if( (old = IsClosed(x, y)) != null )
		{
			for( i = 0; i < 8; i++ )
			{
				if( node.direct[i] == null )
				{
					node.direct[i] = old;
					break;
				}
			}
			
			if( g < old.g )
			{
				old.prev = node;
				old.g = g;
				old.f = old.h + old.g;
				
				// �����尡 ������忡 �ִٸ� �׿� ����� ��� ������ ������ ����
				//MakeDown(old);
			}
		}
	
		// ���ο� ����� ������� �����ϰ� ������忡 �߰�
		else
		{
			// ���ο� ��� ����
			child = new Node();
			
			child.prev = node;
			child.g = g;
			child.h = (x-tx)*(x-tx) + (y-ty)*(y-ty);
			child.f = child.h + child.g;
			child.x = x;
			child.y = y;
			
			// ���ο� ��带 ������忡 �߰�
			InsertNode(child);

			for( i = 0; i < 8; i++ )
			{
				if( node.direct[i] == null )
				{
					node.direct[i] = child;
					break;
				}
			}
		}
	}


	//*************************************************************************
	// Name : IsOpen()
	// Desc : �Էµ� ��尡 ����������� �˻�
	//*************************************************************************
	Node IsOpen(int x, int y)
	{
		Node tmp = OpenNode;
		
		while( tmp != null )
		{
			if( tmp.x == x && tmp.y == y )
			{
				return tmp;
			}
			
			tmp = tmp.next;
		}
		
		
		return null;
	}

	//*************************************************************************
	// Name : IsClosed()
	// Desc : �Էµ� ��尡 ����������� �˻�
	//*************************************************************************
	Node IsClosed(int x, int y)
	{
		Node tmp = ClosedNode;
		
		
		while( tmp != null )
		{
			if( tmp.x == x && tmp.y == y )
			{
				return tmp;
			}
			
			tmp = tmp.next;
		}
		
		return null;
	}


	//*************************************************************************
	// Name : InsertNode()
	// Desc : �Էµ� ��带 ������忡 f���� ���� �����Ͽ� �߰�
	//        f���� �������� ���� ���� ������ -> ������ ���
	//*************************************************************************
	void InsertNode(Node src)
	{
		Node old = null, tmp = null;
		
		
		if( OpenNode == null )
		{
			OpenNode = src;
			return;
		}
		
		tmp = OpenNode;
		while( tmp != null && (tmp.f < src.f) )
		{
			old = tmp;
			tmp = tmp.next;
		}
		
		if( old != null )
		{
			src.next = tmp;
			old.next = src;
		}
		else
		{
			src.next = tmp;
			OpenNode = src;
		}
	}
}