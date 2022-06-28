package org.dfpl.lecture.database.assignment45.assignment20011765;
import java.util.*;

@SuppressWarnings("unused")
public class ThreeWayBPlusTree implements NavigableSet<Integer> {

    // Data Abstraction은 예시일 뿐 자유롭게 B+ Tree의 범주 안에서 어느정도 수정가능
    private ThreeWayBPlusTreeNode root;
    private LinkedList<ThreeWayBPlusTreeNode> leafList;

    public static final int M=3;    //3Way B Plus Tree 이므로 M-Way 의 M에 3을 넣어놓고 상수처럼 사용하기 위해 만들었다.
    public static final int max_child=M;    //최대 child 수
    public static final int max_key=M-1;    //최대 key 수
    public static final int min_key=(int)(Math.ceil(M/2.0))-1;  //최소 key 수
    public static final int split_mid=(int)(Math.ceil((M-1)/2.0));  //split 할때 사용할 mid 값
    /**
     * 과제 Assignment4를 위한 메소드:
     *
     * key로 검색하면 root부터 시작하여, key를 포함할 수 있는 leaf node를 찾고 key가 실제로 존재하면 해당 Node를
     * 반환하고, 그렇지 않다면 null을 반환한다. 중간과정을 System.out.println(String) 으로 출력해야 함. 3 way
     * B+ tree에서 1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17 이 순서대로
     * add되었다고 했을 때,
     *
     * 예: getNode(15)을 수행하였을 때
     * > start finding 15
     * > larger than or equal to 9
     * > larger than or equal to 13
     * > larger than or equal to 15
     * > less than 16
     * > 15 found
     * 위의 6 문장을
     * 콘솔에 출력하고 15를 포함한 ThreeWayBPlusTreeNode를 반환함
     *
     *
     * //@param key
     * @return
     */
    //생성자 생성, 처음에 시작할때 ThreeWayBPlusTree 를 전부다 초기화 한다.
    public ThreeWayBPlusTree(){
        this.root=null;
        this.leafList=new LinkedList<>();
    }
    //이진탐색을 한다. 이진탐색을 해도 되는 이유는 Key 들은 정렬되어서 들어가므로 이진트리 탐색이 가능하기 때문이다.
    private int binarySearchNode(int key,List<Integer>keyList){
        int start=0;
        int end=keyList.size()-1;
        int mid;
        int idx=-1;
        //맨 처음거보다 작다면 0번 idx에 넣어야 하므로 0을 반환
        if(key<keyList.get(0))return 0;
        //w맨 뒤에꺼보다 크다면 맨 뒤에 넣어야 하므로 keylist의 size를 반환
        if(key>=keyList.get(end))return keyList.size();
        //속도를 빠르게 하기 위해서 이진트리를 만들었음(KeyList가 커야 3개라서 더 빠른진 잘 모르겠음)
        while(start<=end){
            mid=(start+end)/2;
            //이 사이라면 가운데 껴야 하므로 mid 를 반환
            if(key<keyList.get(mid) && key>=keyList.get(mid-1)){
                idx=mid;
                break;
            }
            //뒤에 있는 것을 암시하므로 뒤만 판단하면 됨.
            else if(key>=keyList.get(mid)){
                start=mid+1;
            }
            else{
                end=mid-1;
            }
        }
        return idx;
    }
    public ThreeWayBPlusTreeNode getNode(Integer key) {
        System.out.println("start finding "+key);
        ThreeWayBPlusTreeNode node=root;
        while(true){
            int pos;
            for (pos = 0; pos < node.getKeyList().size(); pos++) {
                //key 랑 node 값이 같다면 값을 찾은 것이고, B plus tree 에서는 값을 leaf 까지 가야 하므로 leaf 까지 갔는지를 확인한다.
                if (node.getKeyList().get(pos).equals(key) && node.getBottom()) {
                    System.out.println(key + " found");
                    return node;
                }
                //만약 key 값보다 keylist 의 값이 크다면, 그거 왼쪽에 있는 것이기 때문에 그 position의 child 로 이동한다.
                else if (node.getKeyList().get(pos) > key && !node.getBottom()) {
                    // move to left child node
                    System.out.println("less than to " + node.getKeyList().get(pos));
                    node = node.getChildren().get(pos);
                    pos = -1;
                }
                // 0 같은 상황에서는 맨 밑에 있는 leaf node 의 keylist 를 순회해야 한다.
                else if(node.getKeyList().get(pos) > key && node.getBottom()){
                    System.out.println("less than to " + node.getKeyList().get(pos));
                }
                // 18 같은 상황에서는 맨 밑에 있는 leaf node 의 keylist 를 순회해야 한다.
                else if(node.getKeyList().get(pos) < key && node.getBottom()){
                    System.out.println("larger than or equal to " + node.getKeyList().get(pos));
                }
            }
            //반복문을 탈출했다는 것은 pos가 끝까지 갔다는 것이고, 그럼 왼쪽에 있는 것은 아니고 오른쪽에 있거나 아니면 없다는 것이다.
            //없는 경우에는 leaf node 까지 간 것이라는 것을 확인할 수 있다.
            //이런 상황에서는 값이 없는 상황이므로 not found를 출력한다.
            if(node.getBottom()){
                System.out.println(key + " not found");
                return null;
            }
            //right 로 가는 상황은 size 끝까지 같는데도 다 작은 상황밖에 없다. (정렬되어있기 떄문이다.) 그러므로 이 케이스는 따로 예뢰를 둔다.
            System.out.println("larger than or equal to " + node.getKeyList().get(pos - 1));
            node = node.getChildren().get(pos);
        }
    }
    /**
     * 과제 Assignment4를 위한 메소드:
     *
     * inorder traversal을 수행하여, 값을 오름차순으로 출력한다.
     * 1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17 이 순서대로
     * add되었다고 했을 때,
     * 1
     * 2
     * 3
     * 4
     * 5
     * 6
     * 7
     * 8
     * 9
     * 10
     * 11
     * 12
     * 13
     * 14
     * 15
     * 16
     * 17
     * 위와 같이 출력되어야 함.
     * Note: Bottom의 LinkedList 순회를 하면 안됨
     */
    public void inorderTraverse() {
        //inorderTraverse 는 인자가 없는 함수이기 때문에 재귀적으로 인자를 전달해줄수 없다고 판단하여서 Traverse 라는 함수를 만들어서 재귀적으로 순회히도록 구성
        Traverse(root); //root node 부터 시작을 해야 하므로 root node 부터 시작하도록 만들었다.
        // TODO Auto-generated method stub
    }
    public void Traverse(ThreeWayBPlusTreeNode node){
        //node 가 null 이라는 것은 현재 아무것도 안 들어간 상태이므로 그냥 return; 한다.
        if(node==null){
            System.out.println("There is No Root Node!");   //이건 그냥 개인적으로 확인하기 위하여 적은 출력문
            return;
        }
        //중위순회: 왼족 자식 -> 자신 -> 오른쪽 자신
        else{
            for(ThreeWayBPlusTreeNode tree:node.getChildren()){
                if(tree.getKeyList().get(0) < node.getKeyList().get(0)){    //현재 node 의 자식들 중 자신보다 작은 값을 가지고 있는 곳, 즉 왼편을 찾아서 왼편쪽을 또다시 Traverse한다.
                    Traverse(tree);
                }
            }
            //위에서 왼쪽 순회를 완료했기 때문에 자기 자신 호출
            for(Integer e: node.getKeyList()){
                System.out.println(e);
            }
            for(ThreeWayBPlusTreeNode tree:node.getChildren()){
                if(tree.getKeyList().get(0) >= node.getKeyList().get(0)){    //현재 node 의 자식들 중 자신보다 크거나 같은 값을 가지고 있는 곳, 즉, 오른쪽을 Traverse 한다.
                    Traverse(tree);
                }
            }
        }
    }
    @Override
    public Comparator<? super Integer> comparator() {
        // TODO Auto-generated method stub
        return null;
    }

    //first 는 leaflist 의 제일 처음것일 것이기 떄문에 제일 왼쪾으로만 가면 됨.
    @Override
    public Integer first() {
        ThreeWayBPlusTreeNode findfirst=this.root;
        while(!findfirst.getBottom()){
            findfirst=findfirst.getChildren().get(0);
        }
        return findfirst.getKeyList().get(0);
        // TODO Auto-generated method stub
    }
    //last 는 leaflist 의 제일 마지막 것이기 떄문에 제일 오른쪽으로만 가면 됨.
    @Override
    public Integer last() {
        ThreeWayBPlusTreeNode findfirst=this.root;
        while(!findfirst.getBottom()){
            findfirst=findfirst.getChildren().get(findfirst.getChildren().size()-1);
        }
        return findfirst.getKeyList().get(findfirst.getKeyList().size()-1);
        // TODO Auto-generated method stub
    }

    @Override
    public int size() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean isEmpty() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean contains(Object o) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Object[] toArray() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean add(Integer e) {
                // target이 null 이면 e 값이 현재 B+ tree 에 없다는 것이다.
                //만약에 빈 B+ Tree 에 값을 넣는 상황이라면
        ThreeWayBPlusTreeNode target=traverseNode(e);   //중복값 확인(만약 target에 특정 node 가 들어가게 된다면 현재 값은 B+ Tree 에 있는 상황이므로 무시하고 넘겨야 한다.
        if(target==null) {
            if (this.root == null) {
                //root 노드에 대한 세팅, keylist 새로 만들고, keylist 에 e 값 추가, children 새로 만들고, 이 상황에서는 root가 left 이므로 bottom을 true 로 만들고, 부모 노드가 없으므로 부모 노드가 없게 설정
                root = new ThreeWayBPlusTreeNode();
                //이 상황에선 생성자의 세팅과 약간 다른 상황이 있으므로 조작
                root.setBottom(true);
                root.getKeyList().add(e);
                //이 상황에서는 root node 가 left 이므로 leftlist 에 데이터 추가
                leafList.add(root);
            } else {
                //insert 를 하기 위해서는 현재 node의 keylist 에 값을 단순히 저장하고, max_key 조건에 해당되게 된다면 split 해야 한다. split 을 할 때는 부모 노드를 알고 있어야 split 이 가능하고, B+ tree 의 Level 이 얼마인지 모르므로
                //재귀적으로 구성하기로 결정하였다. //재귀적으로 구성하기 위해서 재귀적으로 insert 를 하는 함수를 만들었다. (java에서 재귀를 사용하면 가끔 오류가 생기는데 그 이유는 잘 모르겠다 -> 나중에 공부해볼것)
                root = insertNodeWhenRootisNotNull(0, e, root, root);//처음 설정에 부모를 root 가 아닌 null로 했더니 문제가 발생해서 억지(?) 스럽게 root 를 넣게 되었다.
            }
        }
        // TODO Auto-generated method stub
        return false;
    }
    public ThreeWayBPlusTreeNode insertNodeWhenRootisNotNull(int root_pos,int value,ThreeWayBPlusTreeNode node,ThreeWayBPlusTreeNode parent){
        //이진탐색으로 pos 위치를 선정함. 이진탐색을 써도 되는 이유는 keylist 에는 언제나 정렬된 상황으로 들어갈 것이기 때문에 이진탐색으로 정보를 찾아도 됨(3-way 라서 별로 의미는 없어보인다. 20-way 이상이면 이진트리가 효율적일 것 같음)
        int pos=binarySearchNode(value,node.getKeyList());
        if(!node.getBottom()){
            //현재 node 가 Bottom이 아니면 밑으로 더 내려가서 LinkedList 에 값을 넣어야 하므로 재귀적으로 밑까지 내려간다.
            //keylist의 위치가 결국 child node 의 위치랑 동일하므로 재귀적으로 탐색하면서 값을 집어넣을 곳을 pos라는 위치에 하고, Tree의 Level 을 모르므로 재귀함수를 다시 호출해서 Bottom 까지 내려간다.
            node.getChildren().set(pos,insertNodeWhenRootisNotNull(pos,value,node.getChildren().get(pos),node));
            if(node.getKeyList().size()==M) //max_key+1 이 더 직관적이지만 그냥 M이라고 설정하였다. 이 상황이면 max_key property 를 어긴 것이므로 split 해야 하는 상황이다.
                node=split(root_pos,node,parent);
        }
        else{   //현재 node 가 Bottom 이라면, 이젠 keylist 에 값을 넣어도 되는 상황이므로 값을 집어넣는다.
            node.getKeyList().add(pos,value);
            if(node.getKeyList().size()==M)//max_key+1 이 더 직관적이지만 그냥 M이라고 설정하였다. 이 상황이면 max_key property 를 어긴 것이므로 split 해야 하는 상황이다.
                node=split(root_pos,node,parent);
        }
        return node;//node 값을 반환해야 재귀적으로 돌아왔을때 getchildren의 pos 에 node를 연결시킬 수 있게 된다.
    }
    //변수 중, root_pos 가 있는 이유는 split 을 해서 부모로 올릴 때, 부모 의 key list에 추가해야하기 때문에 0번 부터 넣으면 부모 keylilst에 값이 사라질 수 있기 때문에 부모 key_list에 현재 값이 있는 pos 이후 부터 넣을려고 함
    public ThreeWayBPlusTreeNode split(int root_pos,ThreeWayBPlusTreeNode node,ThreeWayBPlusTreeNode parent){   //이 함수는 재귀적으로 작동하는 것이 아닌, 여기서 만든 node값을 반환하여서 node의 split 을 쉽게 하려고 함.
        //반환할 새로운 Node
        ThreeWayBPlusTreeNode res=new ThreeWayBPlusTreeNode();
        res.setBottom(node.getBottom());    //현재 node 와 Bottom 상황은 동일해야하므로 (split 을 하는 것이기 때문) node 의 Bottom 값을 복사한다.
        if(res.getBottom()){
            res.getKeyList().add(node.getKeyList().get(split_mid)); //leaf 일때 mid포함
            //리스트 iterator 사용해서 해당 index 부터 시작할 수 있도록 함 (divide Node 선택)
            //(remove 작업이 있어서 for each 는 위험하므로 iterator 를 사용한다.)
            ListIterator<Integer>it=node.getKeyList().listIterator(split_mid+1);
            while(it.hasNext()){
                Integer add_remove=it.next();
                res.getKeyList().add(add_remove);
                it.remove();
            }
            //leaf list에 res 값을 넣어야함. 그런데 leaf list 도 정렬되어서 들어가므로 위치를 지정해줘야 함.
            //leaftlist 에 현재 값이 있는지 확인한다.
            if(leafList.indexOf(node)==leafList.size()-1)
                leafList.add(res);
                //주석 달떄 생각해보니 node 가 Bottom 일때를 조건으로 달았는데 당연한 걸 왜 적었는지 모르겠따.
            else
                leafList.add(leafList.indexOf(node)+1,res); //node 의 인덱스를 가지고 와서, 그 다음 인덱스에 값을 집어넣는다.
        }
        else{
            //bottom이 아니면 mid 포함 X
            //동일하게 Listiterator 사용
            ListIterator<Integer>it=node.getKeyList().listIterator(split_mid+1);
            while(it.hasNext()){
                Integer add_remove=it.next();
                res.getKeyList().add(add_remove);
                it.remove();
            }
            //divide 노드에 child를 추가
            //동일하게 Listiterator 사용
            if(!node.getBottom()) {
                ListIterator<ThreeWayBPlusTreeNode> it2 = node.getChildren().listIterator(split_mid + 1);
                while (it2.hasNext()) {
                    ThreeWayBPlusTreeNode add_remove = it2.next();
                    res.getChildren().add(add_remove);
                    add_remove.setParent(res);
                    it2.remove();
                }
            }
        }
        if(node==this.root){    //현재 node 가 root node 이면 split 을 하기 위해 새로운 parent 가 필요하다.
            ThreeWayBPlusTreeNode case1 = new ThreeWayBPlusTreeNode();
            case1.getKeyList().add(node.getKeyList().get(split_mid));//mid가 root 가 되는 상황이므로 keylist 에 넣는다.
            //노드에 있는 mid 값은 내려가야 하므로 node 에서 mid에 해당하는 값을 지운다.
            node.getKeyList().remove(split_mid);
            case1.getChildren().add(node);
            //node 를 내렸으므로 이젠 node 의 부모는 case1이 되어야 한다.
            node.setParent(case1);
            //위에서 만든 res 가 right 이므로 이미 앞에서 node 는 left 로 갔으므로 이젠 newnode 를 추가한다.
            case1.getChildren().add(res);
            res.setParent(case1);
            //split 을 완료하였고, root 는 case1이므로 case1을 결과로 반환해준다.
            return case1;
        }
        else{   //기존 parent 를 사용해서 만들어야 한다. ( 이 상황때문에 인자에 parent node를 넣어야한다)
            int pointer=parent.getKeyList().size();
            for(int i=pointer;i>root_pos;i--){   //root_pos 기준 오른쪽들꺼 한칸씩 오른쪽으로 이동
                parent.getKeyList().add(i,parent.getKeyList().get(i-1));//왼쪽꺼를 가지고 와서 오른쪽으로 한칸씩 옮긴다.
                parent.getKeyList().remove(i-1); //맨 뒤에서부터 처리하기 때문에 여기서의 remove 는 위험하지 않음 (for each 문을  사용하기에는 반복문 범위를 설정하기 귀찮아서 for문 사용, 시간이 되면 for each 로 수정하고 싶음)
                parent.getChildren().add(i+1,parent.getChildren().get(i));//children은 한칸씩 오른쪽으로 생겼기 때문에 (말이 좀 이상하네용...) i+1로 옮긴다.
                parent.getChildren().remove(i); //맨 뒤에서부터 처리하기 때문에 여기서의 remove 는 위험하지 않음 (for each 문을  사용하기에는 반복문 범위를 설정하기 귀찮아서 for문 사용, 시간이 되면 for each 로 수정하고 싶음)
            }
            parent.getKeyList().add(root_pos,node.getKeyList().get(split_mid)); //부모에 mid 에 대한 정보를 집어넣는다 (mid 가 위로 올라가기 때문)
            node.getKeyList().remove(split_mid); //node 에 이젠 mid 인덱스를 지운다(이미 부모로 보냈기 때문)
            parent.getChildren().add(root_pos+1,res);//left에 값을 넣는다.
            res.setParent(parent);  //res 의 부모도 다시 재설정(linking)
        }
        return node;    //다 만들고 나서 node 를 반환한다.
    }
    @Override
    public boolean remove(Object o) {
        //현재 값이 있는 건지 아닌지를 확인해야 한다. getNode 를 이용하면 현재 값이 있는지 없는지를 알 수 있다.
        //getNode 를 수정해서 Text 가 안나오게 만들고 return 값을 node로 하면 remove에 용이할 것으로 보인다.
        //메소드 오버로딩을 사용하려고 했는데 @Override 때문에 안되는것 같다. 나중에 시간이 되면 찾아봐야겠다.
        //다운캐스팅 이용
        /*
        System.out.println("Remove Goal"+o);
        ThreeWayBPlusTreeNode target=traverseNode((Integer) o);
        System.out.println("target!");
        for(Integer e: target.getKeyList()){
            System.out.println(e);
        }
        System.out.println("target! parent Keylist");
        for(Integer e: target.getParent().getKeyList()){
            System.out.println(e);
        }
        System.out.println("target parent child keylist");
        for(ThreeWayBPlusTreeNode e:target.getParent().getChildren()){
            for(Integer f:e.getKeyList()){
                System.out.println(f);
            }
            System.out.println();
        }

         */
        ThreeWayBPlusTreeNode target=traverseNode((Integer) o);
        //System.out.println(target.getParent().getChildren().indexOf(target));
        //만약 현재 node 의 맨 앞의 값이면 부모를 업데이트 해서 값을 갱신해야 한다.
        if(target!=null) {
                if (target.getKeyList().indexOf((Integer) o) == 0) {
                    //특정 node 에 맨 앞에 있는 값을 제거 해야 하는 상황이 된다면 부모를 갱신해야 한다.
                    if(target.getKeyList().size()>1)    //3-way 라서 min_key 가 1이기 때문에 만약 1개인 경우에서 제거를 해애 하는 경우 부모 갱신이 아닌, Merge를 해야 한다. 그래서 조건을 달았다.
                        updateParent(target.getParent(), (Integer) o, target.getKeyList().get(1));
                }
                target.getKeyList().remove(o);      //target의 노드에 해당 값을 제거한다. (target은 tarverseNode 로 인해 leaf 에 있는 값이 된다.)
                while (target.getParent() != null) {    //밑에서부터 위로 올라가면서 Balancing 을 진행한다.
                    if (target.getKeyList().size() < min_key) { //min_key property 안지키면
                        int pos=target.getParent().getChildren().indexOf(target);   //target의 부모의 자식을 탐색하면 target은 무조건 존재한다.(잘 지워졌다는 가정 하에) 이런 상황에서 target이 부모의 자식에서의 인덱스를 찾는다.
                        if(pos==0){ //만약 인덱스가 젤 처음이면(즉, 왼쪽에서 빌릴 것이 없는 상황이면)
                            if(target.getParent().getChildren().size()>1){      //위에와 동일한 이유로, min_key 는 1이기 때문에 child의 개수가 1개일수도있다. 이런 경우에는 BorrowFromRight가 불가능하다.
                                if(target.getParent().getChildren().get(pos+1).getKeyList().size()>min_key){    //Min_key Property 가 지켜진 Node가 오른쪽 Node이면
                                    borrowFromRight(target.getParent(),pos);        //오른쪽에서 빌려온다.
                                }
                                else{
                                    mergeNode(target.getParent(),pos+1,pos);    //Min_key Property 가 지켜지지 않은 상황이면 Merge 를 통해 작업을 진행한다.
                                }
                            }
                        }
                        else if(pos==target.getParent().getKeyList().size()){       //맨 오른쪽에 있는 값이라서 왼쪽에서만 빌려야 하는 상황을 말한다.
                            if (target.getParent().getChildren().get(pos - 1).getKeyList().size()>min_key) {    //Min_Key Property 를 지킨 상황이면
                                borrowFromLeft(target.getParent(), pos);        //왼쪾에서 빌려서 작업을 진행한다.
                            } else {
                                mergeNode(target.getParent(), pos, pos - 1);//Min_Key Property 를 지키지 않은 상황이면 Merge 를 통해 진행을 한다.
                            }
                        }
                        else { //3-way 이므로 맨 위의 케이스는 인덱스가 0, else if 케이스는 인덱스가 2, else 인 상황은 인덱스가 1인 상황이다. 단지 일반화를 위해서 이렇게 만들어놓았다.
                            if (target.getParent().getChildren().get(pos - 1).getKeyList().size()>min_key) {    ////Min_Key Property 를 지킨 상황이면 우선 왼쪽부터 봐서 빌리는 것을 시작한다.
                                borrowFromLeft(target.getParent(), pos);
                            } else if (target.getParent().getChildren().get(pos + 1).getKeyList().size()>min_key) {////Min_Key Property 를 지킨 상황이면 그 다음으로는 오른쪽을 보면서 빌린다.
                                borrowFromRight(target.getParent(), pos);
                            } else {
                                // violate, merge with left sibling
                                mergeNode(target.getParent(), pos, pos - 1);    //둘다 빌릴 수 없는 상황이면 바로 Merge 를 진행한다.
                            }
                        }
                    }
                    target = target.getParent();        //Balancing은 한번만 할 수도 있지만, 여러번 해야 하는 상황이 발생할수도 있기 때문에 getParent 를 하면서 위로 올라가면서 Balancing이 끝날때까지 진행한다.
                }
            return true;
        }
        // TODO Auto-generated method stub
        return false;
    }
    public void borrowFromLeft(ThreeWayBPlusTreeNode node, int pos) {       //왼쪽에서 빌리는 상황을 함수로 만들었다. (중복되는 경우가 많았기 때문)
        Integer PLV = node.getKeyList().get(pos - 1);       //DB Sharable 에 있는 변수명을 그대로 사용. node의 keylist에서 왼쪽 값을 가지고 온다. 즉, left sibling 이 된다.
        Integer LV = node.getChildren().get(pos - 1).getKeyList().get(node.getChildren().get(pos - 1).getKeyList().size() - 1);
        node.getChildren().get(pos).getKeyList().add(0, PLV); //PLV 에서 T 로 값을 이동시킨다.
        node.getKeyList().set(pos - 1, LV); //LV 에서 T 로 값을 이동시킨다.
        node.getChildren().get(pos - 1).getKeyList().remove(LV);    //이동시킨후 LV 값을 지운다.
        if (node.getChildren().get(pos).getBottom()) {
            node.getChildren().get(pos).getKeyList().set(0, LV);    //만약 leaf 였으면 이젠 값을 저장해서 Borrow 한 작업을 완료한다.
        } else {
            ThreeWayBPlusTreeNode LC = node.getChildren().get(pos - 1).getChildren().get(node.getChildren().get(pos - 1).getChildren().size() - 1); //Bottom 즉, leaf 가 아니면 자식을 움직인다.
            node.getChildren().get(pos).getChildren().add(0, LC);
            LC.setParent(node.getChildren().get(pos));
            node.getChildren().get(pos - 1).getChildren().remove(LC);
        }
    }
    public void borrowFromRight(ThreeWayBPlusTreeNode node, int pos) {
        Integer PRV = node.getKeyList().get(pos);//DB Sharable 에 있는 변수명을 그대로 사용. node의 keylist에서 왼쪽 값을 가지고 온다. 즉, right sibling 이 된다.
        Integer RV = node.getChildren().get(pos + 1).getKeyList().get(0);
        node.getChildren().get(pos).getKeyList().add(PRV); // PRV에 있던 값을 T로 옮긴다.
        node.getKeyList().set(pos, RV); // RV에 있던 값을 PRV로 옮긴다.
        node.getChildren().get(pos + 1).getKeyList().remove(RV);
        if (node.getChildren().get(pos).getBottom()) {
            updateParent(node, RV, node.getChildren().get(pos + 1).getKeyList().get(0));        //오른쪽은 왼족과는 다르게 진행되므로(왼쪽 우선으로 코드를 작성했기 때문) 부모의 key 를 바꾼다.
        } else {
            ThreeWayBPlusTreeNode RC = node.getChildren().get(pos + 1).getChildren().get(0);    //Bottom 즉, leaf 가 아니면 자식을 움직인다.
            node.getChildren().get(pos).getChildren().add(RC);
            RC.setParent(node.getChildren().get(pos));
            node.getChildren().get(pos + 1).getChildren().remove(RC);
        }
    }
    public void mergeNode(ThreeWayBPlusTreeNode node, int rc, int lc) {
        //왼쪽 Child에 합치는 작업을 진행한다.
        ThreeWayBPlusTreeNode RC = node.getChildren().get(rc);        //오른쪽 Child를 가지고 온다.
        ThreeWayBPlusTreeNode LC = node.getChildren().get(lc);         //왼쪾 child를 가지고 온다.
        if (!RC.getBottom()) {
            LC.getKeyList().add(node.getKeyList().get(lc)); //만약 interval Node 일때의 합치는 작업을 진행한다.
        }
        LC.getKeyList().addAll(RC.getKeyList()); // Right Child의 KeyList를 Left Child 의 KeyList로 옮긴다.
        LC.getChildren().addAll(RC.getChildren()); // Right Child의 Child 들을 Left Child 의  Child 들로 옮긴다.
        for (ThreeWayBPlusTreeNode child : RC.getChildren()) child.setParent(LC);   //Rc Child에 있는 값들에게 Left Child 를 부모로 지정한다.
        node.getChildren().remove(RC);     //다 옮겼으므로 RightChild를 제거
        node.getKeyList().remove(lc);       //다 옮겼으므로 LeftChild의 Pos에 있는 값을 제거한다.
        leafList.remove(RC);
        if (root == node && root.getChildren().size() == 1) {       //root를 갱신해야 하는 상황이 발생하면 즉, root가 node인 상황이고, root의 children 의 size가 1인 상황이면 root 를 RC 로 바꾼다.
            root = LC;
        }
    }
    public void updateParent(ThreeWayBPlusTreeNode node, Integer prev, Integer cur) {   //부모 갱신 작업을 진행한다.
        if (node != null) { //node가 null이면 갱신할 수 없는 상황 사용한 이유는 updateParent 를 재귀적으로 구성할 것이기 때문)
            if(node.getKeyList().indexOf(prev)>-1) {    //만약 prev가 node 의 keylist안에 있는 상황이면, 갱신을 한다. -1 초과로 한 이유는 인덱스가 없으면 -1을 반환하기 때문이다.
                node.getKeyList().set(node.getKeyList().indexOf(prev), cur);
                return; //갱신했으므로 return;
            }
            updateParent(node.getParent(), prev, cur);
        }
    }
    public ThreeWayBPlusTreeNode traverseNode(Integer target){
        ThreeWayBPlusTreeNode node=root;
        if(node==null)return null;
        while(true){
            int pos;
            for (pos = 0; pos < node.getKeyList().size(); pos++) {
                //key 랑 node 값이 같다면 값을 찾은 것이고, B plus tree 에서는 값을 leaf 까지 가야 하므로 leaf 까지 갔는지를 확인한다.
                if (node.getKeyList().get(pos).equals(target) && node.getBottom()) {
                    return node;
                }
                //만약 key 값보다 keylist 의 값이 크다면, 그거 왼쪽에 있는 것이기 때문에 그 position의 child 로 이동한다.
                else if (node.getKeyList().get(pos) > target && !node.getBottom()) {
                    // move to left child node
                    node = node.getChildren().get(pos);
                    pos = -1;
                }
            }
            //반복문을 탈출했다는 것은 pos가 끝까지 갔다는 것이고, 그럼 왼쪽에 있는 것은 아니고 오른쪽에 있거나 아니면 없다는 것이다.
            //없는 경우에는 leaf node 까지 간 것이라는 것을 확인할 수 있다.
            //이런 상황에서는 값이 없는 상황이므로 not found를 출력한다.
            if(node.getBottom()){
                return null;
            }
            //right 로 가는 상황은 size 끝까지 같는데도 다 작은 상황밖에 없다. (정렬되어있기 떄문이다.) 그러므로 이 케이스는 따로 예뢰를 둔다.
            node = node.getChildren().get(pos);
        }
    }
    @Override
    public boolean containsAll(Collection<?> c) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends Integer> c) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void clear() {
        // TODO Auto-generated method stub

    }

    @Override
    public Integer lower(Integer e) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Integer floor(Integer e) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Integer ceiling(Integer e) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Integer higher(Integer e) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Integer pollFirst() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Integer pollLast() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Iterator<Integer> iterator() {
        //inner class 생성 후 구현
        Iterator<Integer> myiterator = new Iterator<Integer>() {
            ThreeWayBPlusTreeNode node=leafList.getFirst(); //처음에는 leaftlist 의 맨 처음을 가지고 온다.
            int key_pos=0;
            @Override
            public boolean hasNext() {  //만약 현재 node 가 null 이 아니면 true 를 반환, 아니면 false 를 반환한다.
                if(node!=null)return true;
                return false;
            }

            @Override
            public Integer next() {
                Integer e=0;
                //현재 key_pos 의 값을 가지고 올 수 있는 상황이면
                if(key_pos<node.getKeyList().size()){
                    //node 의 keylist에서 key_pos 값을 가지고 오고 key_pos 값을 늘린다.
                    e=node.getKeyList().get(key_pos++);
                    //만약 key_pos 가 끝까지 갔을 떄
                    if(key_pos==node.getKeyList().size()){
                        //만약 현재가 leaftist 의 마지막 node 였다면 다음 것은 없으므로 null로 바꾼다.
                        if(node.equals(leafList.getLast())){
                            node=null;
                        }
                        else{
                            //마지막이 아니면 그 다음 node 로 이동하고 key_pos 를 0으로 바꾼다.
                            if(node.equals(leafList.get(leafList.indexOf(node)+1))){
                                if(leafList.get(leafList.indexOf(node)+1).equals(leafList.getLast())){
                                    node=null;
                                }
                                else {
                                    key_pos = 0;
                                }
                            }else {
                                node = leafList.get(leafList.indexOf(node) + 1);
                                key_pos = 0;
                            }
                        }
                    }
                }
                //아까 나왔던 int 값을 반환한다.
                return e;
            }
        };
        //내가 만든 iterator 를 반환한다.
        return myiterator;
    }

    @Override
    public NavigableSet<Integer> descendingSet() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Iterator<Integer> descendingIterator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public NavigableSet<Integer> subSet(Integer fromElement, boolean fromInclusive, Integer toElement,
                                        boolean toInclusive) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public NavigableSet<Integer> headSet(Integer toElement, boolean inclusive) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public NavigableSet<Integer> tailSet(Integer fromElement, boolean inclusive) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SortedSet<Integer> subSet(Integer fromElement, Integer toElement) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SortedSet<Integer> headSet(Integer toElement) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SortedSet<Integer> tailSet(Integer fromElement) {
        // TODO Auto-generated method stub
        return null;
    }

}
