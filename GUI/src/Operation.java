/**
 *
 * @author Gustavo
 */
public class Operation {
  int type, val1, val2, flag;
  char cType;
  
   Operation(){
       
   }
  
  Operation(int type, int val1, int val2){
    this.type = type;
    this.val1 = val1;
    this.val2 = val2;
    
    switch (this.type){
      case 0:
        cType = '+';
        break;
      case 1:
        cType = '-';
        break;
      case 2:
        cType = '*';
        break;
      case 3:
        cType = '/';
        break;
    }
  }

  int solve(){
    switch (type){
      case 0:
        return val1 + val2;
      case 1:
        return val1 - val2;
      case 2:
        return val1 * val2;
      case 3:
        return val1 / val2;
      default:
        return 0;
    }
  }
}
