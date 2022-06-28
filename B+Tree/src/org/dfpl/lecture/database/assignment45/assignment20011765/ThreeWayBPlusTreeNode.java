package org.dfpl.lecture.database.assignment45.assignment20011765;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class ThreeWayBPlusTreeNode {

    // Data Abstraction은 예시일 뿐 자유롭게 B+ Tree의 범주 안에서 어느정도 수정가능
    private ThreeWayBPlusTreeNode parent;
    private List<Integer> keyList;
    private List<ThreeWayBPlusTreeNode> children;
    private Boolean isBottom;   //현재 Note 가 Bottom 즉, leaf 인지 확인한다. (leaf 는 Tree에서 씌여서 헷갈릴까봐 Bottom으로 사용)

    //Node 생성자 기본적인 세팅들을 한다.
    public ThreeWayBPlusTreeNode(){
        this.parent=null;
        this.keyList=new ArrayList<>();
        this.children=new ArrayList<>();
        this.isBottom=false;
    }
    //Getter and Setter
    public Boolean getBottom() {
        return isBottom;
    }

    public void setBottom(Boolean bottom) {
        isBottom = bottom;
    }

    public ThreeWayBPlusTreeNode getParent() {
        return parent;
    }

    public void setParent(ThreeWayBPlusTreeNode parent) {
        this.parent = parent;
    }

    public List<Integer> getKeyList() {
        return keyList;
    }

    public void setKeyList(List<Integer> keyList) {
        this.keyList = keyList;
    }

    public List<ThreeWayBPlusTreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<ThreeWayBPlusTreeNode> children) {
        this.children = children;
    }
}