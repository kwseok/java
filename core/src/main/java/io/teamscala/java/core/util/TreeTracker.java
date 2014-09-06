package io.teamscala.java.core.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 트리 트레커.
 *
 * @param <T> 처리할 클래스 타입
 */
public class TreeTracker<T> {

    /**
     * 기본 부모 프로퍼티 이름
     */
    public static final String DEFAULT_PARENT_PROPERTY_NAME = "parent";

    /**
     * 기본 자식 프로퍼티 이름
     */
    public static final String DEFAULT_CHILDREN_PROPERTY_NAME = "children";


    // 부모 메서드 이름
    private String parentMethodName;

    // 자식 메서드 이름
    private String childrenMethodName;


    // 인덱스
    private int index;

    // 현재 처리중인 깊이
    private int depth;

    // 마지막으로 처리된 깊이
    private int lastDepth;

    // 깊이의 인덱스들
    private List<Integer> indexesInDepth = new ArrayList<>();


    /**
     * 기본 생성자.
     */
    public TreeTracker() {
        this(null, null);
    }

    /**
     * 생성자.
     *
     * @param parentPropertyName   부모 프로퍼티 이름
     * @param childrenPropertyName 자식 프로퍼티 이름
     */
    public TreeTracker(String parentPropertyName, String childrenPropertyName) {
        setParentPropertyName(parentPropertyName);
        setChildrenPropertyName(childrenPropertyName);
    }

    /**
     * 부모 프로퍼티 이름 세팅
     *
     * @param parentPropertyName 부모 프로퍼티 이름
     */
    public void setParentPropertyName(String parentPropertyName) {
        if (parentPropertyName == null) {
            parentPropertyName = DEFAULT_PARENT_PROPERTY_NAME;
        }
        this.parentMethodName = "get" + StringUtils.capitalize(parentPropertyName);
    }

    /**
     * 자식 프로퍼티 이름 세팅
     *
     * @param childrenPropertyName 자식 프로퍼티 이름
     */
    public void setChildrenPropertyName(String childrenPropertyName) {
        if (childrenPropertyName == null) {
            childrenPropertyName = DEFAULT_CHILDREN_PROPERTY_NAME;
        }
        this.childrenMethodName = "get" + StringUtils.capitalize(childrenPropertyName);
    }

    /**
     * 상태를 초기화한다.
     */
    public void clear() {
        this.index = 0;
        this.depth = 0;
        this.lastDepth = -1;
        this.indexesInDepth.clear();
    }

    /**
     * 부모 방향으로 추적하여 리스트로 반환한다.
     *
     * @param node      기준 객체
     * @param ascending 추적방향이 오름차순인지 여부
     * @return 컬렉션
     * @throws Exception {@link java.lang.Exception}
     */
    public List<T> trackingParent(T node, boolean ascending) throws Exception {
        final List<T> results = new ArrayList<>();
        TreeHandler<T> handler = new TreeHandler<T>() {
            @Override
            protected boolean doHandle(T node, int index, int depth, int indexOfDepth, int lastDepth) {
                results.add(node);
                return true;
            }
        };
        trackingParent(node, handler, ascending);
        return results;
    }

    /**
     * 자식 방향으로 추적하여 리스트로 반환한다.
     *
     * @param node 기준 객체
     * @return 컬렉션
     * @throws Exception {@link java.lang.Exception}
     */
    public List<T> trackingChildren(T node) throws Exception {
        final List<T> results = new ArrayList<>();
        TreeHandler<T> handler = new TreeHandler<T>() {
            @Override
            protected boolean doHandle(T node, int index, int depth, int indexOfDepth, int lastDepth) {
                results.add(node);
                return true;
            }
        };
        trackingChildren(node, handler);
        return results;
    }

    /**
     * 부모 방향으로 추적한다.
     *
     * @param node      기준 객체
     * @param handler   핸들러
     * @param ascending 추적방향이 오름차순인지 여부
     * @throws Exception {@link java.lang.Exception}
     */
    public void trackingParent(T node, TreeHandler<T> handler, boolean ascending) throws Exception {
        if (node == null) throw new IllegalArgumentException("Obj must not be null");
        if (handler == null) throw new IllegalArgumentException("Handler must not be null");
        clear();
        trackingParent(node, handler, ascending, 0);
    }

    /**
     * 자식 방향으로 추적한다.
     *
     * @param node    기준 객체
     * @param handler 핸들러 객체
     * @throws Exception {@link java.lang.Exception}
     */
    public void trackingChildren(T node, TreeHandler<T> handler) throws Exception {
        if (node == null) throw new IllegalArgumentException("Obj must not be null");
        if (handler == null) throw new IllegalArgumentException("Handler must not be null");
        clear();
        trackingChildren(node, handler, 0);
    }

    // Protected methods

    /**
     * 부모 방향으로 추적한다.
     *
     * @param node      기준 객체
     * @param handler   핸들러
     * @param ascending 추적방향이 오름차순인지 여부
     * @param depth     깊이 처리를 하지 않는다; 항상 '0' 이다.
     * @throws Exception {@link java.lang.Exception}
     */
    @SuppressWarnings("unchecked")
    protected void trackingParent(T node, TreeHandler<T> handler, boolean ascending, int depth) throws Exception {
        if (!ascending || handle(node, handler, 0)) {
            T parent;
            try {
                parent = (T) node.getClass().getMethod(parentMethodName).invoke(node);
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
            if (parent != null) { // 부모 객체로 재귀호출.
                trackingParent(parent, handler, ascending, 0);
            }
            if (!ascending) handle(node, handler, 0);
        }
    }

    /**
     * 자식 방향으로 추적한다.
     *
     * @param node    기준 객체
     * @param handler 핸들러
     * @param depth   깊이
     * @throws Exception {@link java.lang.Exception}
     */
    @SuppressWarnings("unchecked")
    protected void trackingChildren(T node, TreeHandler<T> handler, int depth) throws Exception {
        if (!handle(node, handler, depth)) return;
        Collection<T> children;
        try {
            children = (Collection<T>) node.getClass().getMethod(childrenMethodName).invoke(node);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        if (children != null && !children.isEmpty()) {
            handler.doEnterDepth(node, getIndex(), getIndexInDepth(), getDepth(), getLastDepth());
            for (T child : children) { // 자식 객체들로 재귀호출.
                trackingChildren(child, handler, depth + 1);
            }
            handler.doLeaveDepth(node, getIndex(), getIndexInDepth(), getDepth(), getLastDepth());
        }
    }

    /**
     * 핸들.
     *
     * @param node    기준 객체
     * @param handler 핸들러.
     * @param depth   깊이
     * @return boolean
     * @throws Exception {@link java.lang.Exception}
     */
    protected boolean handle(T node, TreeHandler<T> handler, int depth) throws Exception {
        this.depth = depth;
        if (!handler.doHandle(node, getIndex(), getIndexInDepth(), getDepth(), getLastDepth())) return false;
        this.index++;
        incrementIndexOfDepth();
        this.lastDepth = depth;
        return true;
    }

    /**
     * @return 인덱스.
     */
    protected int getIndex() {
        return index;
    }

    /**
     * @return 깊이.
     */
    protected int getDepth() {
        return depth;
    }

    /**
     * @return 마지막 깊이.
     */
    protected int getLastDepth() {
        return lastDepth;
    }

    /**
     * @return 현재 깊이의 인덱스.
     */
    protected int getIndexInDepth() {
        return ((indexesInDepth.size() > depth) ? indexesInDepth.get(depth) : 0);
    }

    /**
     * 현재 깊이의 인덱스를 증가시킨다.
     */
    protected void incrementIndexOfDepth() {
        while (indexesInDepth.size() <= depth) indexesInDepth.add(0);
        indexesInDepth.set(depth, indexesInDepth.get(depth) + 1);
    }


    // 테스트
    public static void main(String[] args) throws Exception {
        @SuppressWarnings("unused")
        class Test {
            private String name;
            private Test parent;
            private List<Test> children = new ArrayList<>();

            public Test(String name) {
                this.name = name;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public Test getParent() {
                return parent;
            }

            public void setParent(Test parent) {
                this.parent = parent;
            }

            public List<Test> getChildren() {
                return children;
            }

            public void setChildren(List<Test> children) {
                this.children = children;
                for (Test child : children) {
                    child.setParent(this);
                }
            }

            public void addChild(Test child) {
                this.children.add(child);
                child.setParent(this);
            }
        }

        Test root = new Test("root");
        Test n1 = new Test("n1");
        Test n2 = new Test("n2");
        Test n3 = new Test("n3");
        root.addChild(n1);
        root.addChild(n2);
        root.addChild(n3);

        Test n11 = new Test("n11");
        Test n12 = new Test("n12");
        Test n13 = new Test("n13");
        Test n14 = new Test("n14");
        n1.addChild(n11);
        n1.addChild(n12);
        n1.addChild(n13);
        n1.addChild(n14);

        Test n21 = new Test("n21");
        Test n22 = new Test("n22");
        Test n23 = new Test("n23");
        n2.addChild(n21);
        n2.addChild(n22);
        n2.addChild(n23);

        Test n31 = new Test("n31");
        Test n32 = new Test("n32");
        Test n33 = new Test("n33");
        Test n34 = new Test("n34");
        Test n35 = new Test("n35");
        n3.addChild(n31);
        n3.addChild(n32);
        n3.addChild(n33);
        n3.addChild(n34);
        n3.addChild(n35);

        new TreeTracker<Test>().trackingParent(n35, new TreeHandler<Test>() {
            @Override
            protected boolean doHandle(Test node, int index, int indexInDepth, int depth, int lastDepth) throws Exception {
                System.out.print(index);
                System.out.print(" : ");
                System.out.print(indexInDepth);
                System.out.print(" : ");
                System.out.print(depth);
                System.out.print(" : ");
                System.out.print(lastDepth);
                System.out.print(" : ");
                System.out.println(node.getName());
                return true;
            }
        }, true);

        System.out.println("=========================================");
        new TreeTracker<Test>().trackingChildren(root, new TreeHandler<Test>() {
            @Override
            protected boolean doHandle(Test node, int index, int indexInDepth, int depth, int lastDepth) throws Exception {
                System.out.print(index);
                System.out.print(" : ");
                System.out.print(indexInDepth);
                System.out.print(" : ");
                System.out.print(depth);
                System.out.print(" : ");
                System.out.print(lastDepth);
                System.out.print(" : ");
                System.out.println(node.getName());
                return true;
            }
        });
    }
}