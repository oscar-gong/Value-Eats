import styled from "styled-components";
import { TextField } from "@material-ui/core";

export const TextFieldStyled = styled(TextField)`
  & label.Mui-focused {
    color: #ff8e53;
  }
  & .MuiOutlinedInput-root {
    &.Mui-focused fieldset {
      border-color: #ff8e53;
    }
  }
`;
