export interface ErrorResponse {
    type: string;
    title: string;
    status: number;
    detail: string;
    instance: string;
    ["invalid-params"]: InvalidParam[];
}

export interface InvalidParam {

    object: string;
    message: string;
    field: string;
    rejectedValue: string;
}