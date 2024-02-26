import { AbstractControl, AsyncValidatorFn, ValidationErrors } from "@angular/forms";
import { UsersService } from "../services/users.service";
import { Observable, map } from "rxjs";

export class EmailExistsValidator {
  static validate(userService: UsersService): AsyncValidatorFn {
    return (control: AbstractControl) : Observable<ValidationErrors | null> => {
      return userService.checkIfEmailExists(control.value).pipe(
        map((result:boolean) => result ? {emailAlreadyExists: true}: null)
      )
    }
  }
}
