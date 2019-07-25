package pac_man;
//******************************************************************************
// File Name	: AStar.java
// Description	: A* 알고리즘을 사용한 길찾기 클래스
// Create		: 2003/04/01 JongHa Woo
// Update		:
//******************************************************************************
public class AStar
{
	// 열린 노드, 닫힌 노드 리스트
	Node	OpenNode, ClosedNode;
	
	// 맵 배열
	int		iMap[];
	
	// 최대 루핑 회수
	static final int LIMIT_LOOP = 1000;
	
	//*************************************************************************
	// Name : AStar()
	// Desc : 생성자
	//*************************************************************************
	AStar(int iM[])
	{
		// 맵 배열을 저장
		iMap = iM;
		
		OpenNode = null;
		ClosedNode = null;
	}

	//*************************************************************************
	// Name : ResetPath()
	// Desc : 이전에 생성된 경로를 제거
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
	// Desc : 시작위치와 목표위치를 입력 받아 경로노드 리스트를 반환
	//*************************************************************************
	Node FindPath(int sx, int sy, int tx, int ty)
	{
		Node	src, best = null;
		int		count = 0;
		
		// 처음 시작노드 생성
		src = new Node();
		src.g = 0;
		src.h = (tx-sx)*(tx-sx) + (ty-sy)*(ty-sy);  
		src.f = src.h;
		src.x = sx;
		src.y = sy;
		
		// 시작노드를 열린노드 리스트에 추가
		OpenNode = src;
		
		// 길찾기 메인 루프
		// 최대 반복 회수가 넘으면 길찾기 중지
		while( count < LIMIT_LOOP )
		{
			// 열린노드가 없다면 모든 노드를 검색했으므로 길찾기 중지
			if( OpenNode == null )
			{
				return best;
			}
			
			
			// 열린노드의 첫번째 노드를 가져오고 열린노드에서 제거
			best = OpenNode;
			OpenNode = best.next;
			
			// 가져온 노드를 닫힌노드에 추가
			best.next = ClosedNode;
			ClosedNode = best;
			
			
			// 현재 가져온 노드가 없다면 길찾기 중지
			if( best == null )
			{
				return null;
			}
			
			// 현재 가져온 노드가 목표모드라면 길찾기 성공
			if( best.x == tx && best.y == ty )
			{
				return best;
			}
			
			// 현재 노드와 인접한 노드들로 확장하여 열린노드로 추가
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
	// Desc : 입력받은 노드의 인접한 노드들로 확장
	//*************************************************************************
	char MakeChild(Node node, int tx, int ty)
	{
		int		x, y;
		char	flag = 0;
		char	cc[] = {0, 0, 0, 0, 0, 0, 0, 0};
		
		x = node.x;
		y = node.y;
		
		// 인접한 노드로 이동가능한지 검사
		cc[0] = IsMove(x  , y+1);   //밑
		cc[2] = IsMove(x-1, y  );   //왼쪽
		cc[4] = IsMove(x  , y-1);   //위
		cc[6] = IsMove(x+1, y  );   //오른쪽
		
		// 이동가능한 방향이라면 노드를 생성하고 평가값 계산
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
		/*대각선 방향 경로
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
	// Desc : 이동가능한 위치인지 검사
	//*************************************************************************
	char IsMove(int x, int y)
	{
		// 이동 불가능한 위치인지 검사
		if( x < 0 || x > 13 || y < 0 || y > 13 || (iMap[y*14 + x] == 1) )
		{
			return 0;
		}
		
		return 1;
	}


	//*************************************************************************
	// Name : MakeChildSub()
	// Desc : 노드를 생성. 열린노드나 닫힌노드에 이미 있는 노드라면 
	//        이전값과 비교하여 f가 더 작으면 정보 수정
	//        닫힌노드에 있다면 그에 연결된 모든 노드들의 정보도 같이 수정
	//*************************************************************************
	void MakeChildSub(Node node, int x, int y, int tx, int ty)
	{
		Node	old = null, child = null;
		int		i;
		int		g = node.g + 1;
				
		// 현재노드가 열린 노드에 있고 f가 더 작으면 정보 수정
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
		
		
		
		// 현재노드가 닫힌 노드에 있고 f가 더 작으면 정보 수정
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
				
				// 현재노드가 닫힌노드에 있다면 그에 연결된 모든 노드들의 정보도 수정
				//MakeDown(old);
			}
		}
	
		// 새로운 노드라면 노드정보 생성하고 열린노드에 추가
		else
		{
			// 새로운 노드 생성
			child = new Node();
			
			child.prev = node;
			child.g = g;
			child.h = (x-tx)*(x-tx) + (y-ty)*(y-ty);
			child.f = child.h + child.g;
			child.x = x;
			child.y = y;
			
			// 새로운 노드를 열린노드에 추가
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
	// Desc : 입력된 노드가 열린노드인지 검사
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
	// Desc : 입력된 노드가 닫힌노드인지 검사
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
	// Desc : 입력된 노드를 열린노드에 f값에 따라 정렬하여 추가
	//        f값이 높은것이 제일 위에 오도록 -> 최적의 노드
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