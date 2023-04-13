import type {Meta, StoryObj} from '@storybook/react';

import {RegistrationForm} from "./RegistrationForm";

const meta: Meta<typeof RegistrationForm> = {
    title: "Registration Form",
    component: RegistrationForm,
};

export default meta;
type Story = StoryObj<typeof RegistrationForm>;

export const Default: Story = {
    args: {
        username: {
            value: "Vitalir",
        },
        password: {
            value: "Something",
        },
        setUsername: () => {},
        setPassword: () => {},
    },
}
