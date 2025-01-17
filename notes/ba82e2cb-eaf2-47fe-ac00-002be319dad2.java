/******************************************************************************

                            Online Java Compiler.
                Code, Compile, Run and Debug java program online.
Write your code in this editor and press "Run" button to execute it.

*******************************************************************************/
                          /* Singly Circular Linked List */
class Node{
    int data;
    Node next;
    
    public Node(int data){
        this.data =data;
        this.next =null;
    }
}

class CircularLinkedList{
    private Node head;
    
    public CircularLinkedList(){
        this.head=null;
    }
    
    public void insertAtTail(int data){
        Node new_Node = new Node(data);
        if(head==null){
            head=new_Node;
            new_Node.next=head;
        }else{
            Node temp = head;
            while(temp.next!=head){
                temp=temp.next;
            }
            temp.next=new_Node;
            new_Node.next=head;
        }
    }
    public void insertAtBegining(int data){
        Node new_Node = new Node(data);
        if(head==null){
            head=new_Node;
            new_Node.next=head;
        }else{
            Node temp = head;
            while(temp.next!=head){
                temp=temp.next;
            }
            temp.next=new_Node;
            new_Node.next=head;
            head=new_Node;
        }
    }
    
    public void DeletePosition(int pos){
        if(head==null){
            return;
        }
        Node temp=head;
        Node prev=null;
        if(pos==1){
            while(temp.next!=head){
                temp=temp.next;
            }
            head=head.next;
            temp.next=head;
            return;
        }
        for(int i=0;i<pos-1;i++){
            prev=temp;
            temp=temp.next;
        }
        prev.next=temp.next;
    }
    public void DeletionData(int data){
        if(head==null){
            return;
        }
        Node current = head;
        if(current.data==data){ //if head is Deletion Node.
            Node temp = head;
            while(temp.next!=head){
                temp=temp.next;
            }
            head=head.next;
            temp.next=head;
            return;
        }else{
            current=current.next;
            Node prev =null;
            while(current!=head){
                if(current.data==data){
                    prev.next=current.next;
                }
                prev=current;
                current=current.next;
            }
        }
    }
    public void DisplayData(){
        if(head==null){
            System.out.println("Circular Linked list is Empty!");
            return;
        }
        
        Node temp=head;
        do{
            System.out.print(temp.data + " -> ");
            temp=temp.next;
        }while(temp!=head);
        
        System.out.println();
    }
}
public class Main
{
	public static void main(String[] args) {
		System.out.println("Hello World");
		CircularLinkedList list = new CircularLinkedList();
		list.insertAtTail(10);
		list.insertAtTail(20);
		list.insertAtTail(30);
		list.insertAtTail(40);
		list.insertAtBegining(50);
		list.DisplayData();
// 		list.DeletePosition(4);
// 		list.DeletePosition(1);
        list.DeletionData(30);
        list.DeletionData(50);
		list.DisplayData();
	}
}
