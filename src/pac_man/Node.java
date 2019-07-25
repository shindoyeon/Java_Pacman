package pac_man;

public class Node
{
	int		f;			// f = g+h
	int		h;			// 휴리스틱 값
	int		g;			// 현재까지의 거리
	int		x, y;		// 노드의 위치
	Node	prev;		// 이전 노드
	Node	direct[];	// 인접한 노드
	Node	next;		// 다음 노드	
	//*************************************************************************
	// Name : Node()
	// Desc : 생성자
	//*************************************************************************
	Node()
	{
		direct = new Node[8];    //인접한 8개 노드 
		for( int i = 0; i < 8; i++) direct[i] = null;
	}
}
