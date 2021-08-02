import styled from "styled-components";
import { TextField } from "@material-ui/core";

export const TextFieldStyled = styled(TextField)`
  & label.Mui-focused {
    color: #96ae33;
  }
  & .MuiOutlinedInput-root {
    &.Mui-focused fieldset {
      border-color: #96ae33;
    }
  }
`;
